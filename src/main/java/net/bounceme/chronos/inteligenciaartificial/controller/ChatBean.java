package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
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
import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.dto.ConversationDTO;
import net.bounceme.chronos.inteligenciaartificial.dto.MessageDTO;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;
import net.bounceme.chronos.inteligenciaartificial.util.AIUtils;
import net.bounceme.chronos.inteligenciaartificial.util.JsfHelper;
import reactor.core.Disposable;

@Component
@Named
@ViewScoped
@Slf4j
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
    
    @Getter
    private transient List<MessageDTO> historial;
    
    @Getter
    @Setter
    private transient MessageDTO message;
    
    private transient UserMessage userMessage;
    
    private transient AssistantMessage assistantMessage;
    
	public ChatBean(ChatService chatService, ChatMemory chatMemory) {
		this.chatService = chatService;
		this.chatMemory = chatMemory;
	}

	@PostConstruct
	private void init() {
		
		lastChatResponse = new AtomicReference<>();
		
		selectedConversation = new ConversationDTO();
		
		message = new MessageDTO();
		
		resetHistorial();
	}
	
	public void nuevo() {
		conversationId = UUID.randomUUID().toString();
		selectedConversation = new ConversationDTO();
		selectedConversation.setConversationId(conversationId);
		selectedConversation.setFechaCreacion(new Date());
		
		chatTitle = conversationId;
		mensaje = StringUtils.EMPTY;
		respuesta.setLength(0);
		resetHistorial();
		
		dumpResponse();
		JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Nuevo", "Nueva conversación iniciada");
	}

	@SneakyThrows
	public void guardar() {
		chatService.save(selectedConversation, historial);
		chatTitle = selectedConversation.getNombre();
		
		JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Guardado", "Conversación guardada");
		PrimeFaces.current().executeScript("PF('saveDialog').hide()");
	}
	
	@SneakyThrows
	public void enviar() {
		// No se enviará mensaje a no ser que se inicie una nueva conversación
		if (StringUtils.isBlank(selectedConversation.getConversationId())) {
			JsfHelper.writeMessage(FacesMessage.SEVERITY_WARN, "Atención", "Inicie una nueva conversación");
			return;
		}
		
		cancelSubscription();
		
		// Antes de enviar, limpiar la respuesta
		respuesta.setLength(0);
		status = "INICIADA";
		pollActive = true;
        completionMessageShown = false;
        lastChatResponse.set(null);
        
        // 1. Obtener historial previo
        List<Message> historialMessages = chatMemory.get(conversationId);
        
        // 2. Crear mensaje del usuario
        userMessage = new UserMessage(mensaje);
        message.setRequest(userMessage);
        
        // 3. Construir prompt con historial + nuevo mensaje
        List<Message> todosLosMensajes = new ArrayList<>(historialMessages);
        todosLosMensajes.add(userMessage);
        Prompt prompt = new Prompt(todosLosMensajes);
        
		subscription = chatService.generationStream(prompt, chatClient)
				.doOnNext(chatResponse -> {
                    // Este código se ejecuta en el hilo reactivo por cada fragmento
                    String chunk = chatResponse.getResult().getOutput().getText();
                    respuesta.append(chunk);
                    dumpResponse();
                    
                    // Guardamos la última respuesta para usarla al final
                    lastChatResponse.set(chatResponse);
                    status = "RUNNING";
                })
                .doOnComplete(() -> {
                    status = "COMPLETADA";
                    pollActive = false;
                    
                    processResponse();
                })
                .doOnError(error -> {
                    respuesta.append("\n[Error: " + error.getMessage() + "]");
                    dumpResponse();
                    status = "ERROR";
                    pollActive = false;
                    
                    processResponse();
                })
                .subscribe();
	}

	public void verMensaje() {
		mensaje = message.getRequest().getText();
		htmlContent = JsfHelper.markdown2Html(message.getResponse().getText());
	}
	
	// Método que será llamado por el poll para "forzar" la actualización
    // No hace nada especial, solo sirve para que el poll ejecute una acción JSF
    public void checkUpdates() {
    	if ("COMPLETADA".equals(status) && !completionMessageShown) {
    		completionMessageShown = true;
    		updateChatHistory();
    		
    		PrimeFaces.current().ajax().update("historial");
			JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Completada", "Respuesta completada");
        } else if ("ERROR".equals(status) && !completionMessageShown) {
        	completionMessageShown = true;
    		updateChatHistory();
    		
    		PrimeFaces.current().ajax().update("historial");
            JsfHelper.writeMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió un error");
        }
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
	
	private void processResponse() {
		assistantMessage = new AssistantMessage(respuesta.toString());
	}
	
	private void updateChatHistory() {
		chatMemory.add(conversationId, List.of(userMessage, assistantMessage));
		rebuildHistorial();
	}
	
	private void rebuildHistorial() {
		historial.clear();
		historial.addAll(Optional.ofNullable(chatMemory)
		            .filter(cm -> StringUtils.isNotBlank(conversationId) && completionMessageShown)
		            .map(cm -> cm.get(conversationId))
		            .filter(Objects::nonNull)
		            .map(AIUtils::convertirAParesDTO)
		            .orElse(Collections.emptyList()));
	}

	private void resetHistorial() {
		if (CollectionUtils.isNotEmpty(historial)) {
			historial.clear();
		}
		
		historial = new ArrayList<>();
	}
	
	private void dumpResponse() {
		htmlContent = JsfHelper.markdown2Html(respuesta.toString());
	}
}