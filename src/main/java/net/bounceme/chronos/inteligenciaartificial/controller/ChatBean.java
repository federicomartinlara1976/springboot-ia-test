package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.bounceme.chronos.inteligenciaartificial.dto.ConversationDTO;
import net.bounceme.chronos.inteligenciaartificial.model.ChatMessage;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;
import net.bounceme.chronos.inteligenciaartificial.util.JsfHelper;
import reactor.core.Disposable;

@Component
@Named
@ViewScoped
public class ChatBean extends ChatSelectorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String mensaje;
	
	@Getter
    private volatile String htmlContent;
	
	@Getter
	private transient ChatResponseMetadata chatResponseMetadata;
	
	@Getter 
	private volatile boolean pollActive = false;
	
	private transient ChatService chatService;
	
	private StringBuilder respuesta = new StringBuilder();
	
	private transient Disposable subscription; // para poder cancelar si es necesario
	
	@Getter
	private String status;
	
	private volatile boolean completionMessageShown = false;
	
	@Getter
	private transient Long ellapsedTime;
	
	// Para guardar el último ChatResponse y extraer metadatos al final
    private transient AtomicReference<ChatResponse> lastChatResponse;
    
    private transient ChatMemory chatMemory;
    
    @Getter
    private String conversationId;
    
    @Getter
    private String chatTitle;
    
    @Getter
    @Setter
    private transient ConversationDTO selectedConversation;

	public ChatBean(ChatService chatService) {
		this.chatService = chatService;
	}
	
	@PostConstruct
	private void init() {
		// 1. Crear el repositorio donde se guardan físicamente los mensajes
	    ChatMemoryRepository repository = new InMemoryChatMemoryRepository(); // ← Sustituye a InMemoryChatMemory
		
		chatMemory = MessageWindowChatMemory.builder()  // ← Sustituye a MessageChatMemoryChatHistory
	            .chatMemoryRepository(repository)
	            .maxMessages(20)  // Mantiene los últimos 20 mensajes en contexto
	            .build();
		
		lastChatResponse = new AtomicReference<>();
		
		selectedConversation = new ConversationDTO();
	}
	
	public void nuevo() {
		conversationId = UUID.randomUUID().toString();
		selectedConversation = new ConversationDTO();
		selectedConversation.setConversationId(conversationId);
		selectedConversation.setFechaCreacion(new Date());
		
		chatTitle = conversationId;
		
		JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Nuevo", "Nueva conversación iniciada");
	}
	
	@SneakyThrows
	public void guardar() {
		chatService.save(selectedConversation);
		chatTitle = selectedConversation.getNombre();
		
		JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Guardado", "Conversación guardada");
		PrimeFaces.current().executeScript("PF('saveDialog').hide()");
	}
	
	public List<ConversationDTO> getConversations() {
		return chatService.getConversations();
	}
	
	@SneakyThrows
	public void enviar() {
		cancelSubscription();
		
		// Antes de enviar, limpiar la respuesta
		respuesta.setLength(0);
		status = "INICIADA";
		pollActive = true;
        completionMessageShown = false;
        lastChatResponse.set(null);
        
        // 1. Obtener historial previo
        List<Message> historial = chatMemory.get(conversationId);
        
        // 2. Crear mensaje del usuario
        UserMessage userMessage = new UserMessage(mensaje);
        
        // 3. Construir prompt con historial + nuevo mensaje
        List<Message> todosLosMensajes = new ArrayList<>(historial);
        todosLosMensajes.add(userMessage);
        Prompt prompt = new Prompt(todosLosMensajes);
		
        Long startTime = System.currentTimeMillis();
        
		subscription = chatService.generationStream(prompt, chatClient)
				.doOnNext(chatResponse -> {
                    // Este código se ejecuta en el hilo reactivo por cada fragmento
                    String chunk = chatResponse.getResult().getOutput().getText();
                    respuesta.append(chunk);
                    htmlContent = JsfHelper.markdown2Html(respuesta.toString());
                    
                    // Guardamos la última respuesta para usarla al final
                    lastChatResponse.set(chatResponse);
                    status = "RUNNING";
                })
                .doOnComplete(() -> {
                    // Al completarse, extraemos los metadatos de la última respuesta
                    ChatResponse finalResponse = lastChatResponse.get();
                    if (finalResponse != null) {
                        chatResponseMetadata = finalResponse.getMetadata();
                    }
                    status = "COMPLETADA";
                    pollActive = false;
                    
                    ellapsedTime = System.currentTimeMillis() - startTime;
                    updateChatHistory();
                })
                .doOnError(error -> {
                    respuesta.append("\n[Error: " + error.getMessage() + "]");
                    htmlContent = JsfHelper.markdown2Html(respuesta.toString());
                    status = "ERROR";
                    pollActive = false;
                    
                    ellapsedTime = System.currentTimeMillis() - startTime;
                })
                .subscribe();
	}
	
	// Método que será llamado por el poll para "forzar" la actualización
    // No hace nada especial, solo sirve para que el poll ejecute una acción JSF
    public void checkUpdates() {
    	if ("COMPLETADA".equals(status) && !completionMessageShown) {
			completionMessageShown = true;
			JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Completada", "Respuesta completada");
        } else if ("ERROR".equals(status) && !completionMessageShown) {
            completionMessageShown = true;
            JsfHelper.writeMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió un error");
        }
    }
    
    public List<Message> getHistorial() {
    	if (!Objects.isNull(chatMemory) && StringUtils.isNotBlank(conversationId)) {
	        List<Message> messages = chatMemory.get(conversationId).stream()
	        		.filter(msg -> MessageType.USER.equals(msg.getMessageType()))
	                .map(msg -> new ChatMessage(
	                    msg.getMessageType(), 
	                    msg.getText()
	                ))
	                .collect(Collectors.toList());
	        
	        return messages;
    	}
    	
    	return Collections.emptyList();
    }
    
    // Opcional: cancelar la suscripción al destruir la vista
    @PreDestroy
    public void cleanup() {
        cancelSubscription();
    }

	private void cancelSubscription() {
		if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
	}
	
	private void updateChatHistory() {
		UserMessage userMsg = new UserMessage(mensaje);
		AssistantMessage assistantMsg = new AssistantMessage(respuesta.toString());
		chatMemory.add(conversationId, List.of(userMsg, assistantMsg));
	}
}