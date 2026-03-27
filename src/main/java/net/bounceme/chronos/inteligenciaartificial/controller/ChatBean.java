package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
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

@Component
@Named
@ViewScoped
@Slf4j
public class ChatBean extends AbstractChatBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter
	private transient ChatResponseMetadata chatResponseMetadata;
	
	private transient ChatService chatService;
	
	private StringBuilder respuesta = new StringBuilder();
	
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
		super();
		this.chatService = chatService;
		this.chatMemory = chatMemory;
	}

	@PostConstruct
	private void initChat() {
		selectedConversation = new ConversationDTO();
		
		message = new MessageDTO();
		
		resetHistorial();
		
		loadChat();
	}
	
	private void loadChat() {
		Map<String,String> params = JsfHelper.getExternalContext().getRequestParameterMap();
		String id = params.get("id");
		
		if (StringUtils.isNotBlank(id)) {
			chatService.getConversation(id).ifPresent(c -> {
				selectedConversation = c;
				chatTitle = selectedConversation.getNombre();
				conversationId = c.getConversationId();
				rebuildHistorial();
			});
		}
	}
	
	public void nuevo() {
		conversationId = UUID.randomUUID().toString();
		selectedConversation = new ConversationDTO();
		selectedConversation.setConversationId(conversationId);
		selectedConversation.setNombre(StringUtils.EMPTY);
		selectedConversation.setFechaCreacion(new Date());
		chatService.save(selectedConversation);
		
		chatTitle = conversationId;
		mensaje = StringUtils.EMPTY;
		respuesta.setLength(0);
		resetHistorial();
		
		dumpResponse();
		JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Nuevo", "Nueva conversación iniciada");
	}

	@SneakyThrows
	public void guardar() {
		chatService.save(selectedConversation);
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
		
        // 1. Obtener historial previo
        List<Message> historialMessages = chatMemory.get(conversationId);
        
        // 2. Crear mensaje del usuario
        userMessage = new UserMessage(mensaje);
        message.setRequest(userMessage);
        
        // 3. Construir prompt con historial + nuevo mensaje
        List<Message> todosLosMensajes = new ArrayList<>(historialMessages);
        todosLosMensajes.add(userMessage);
        Prompt prompt = new Prompt(todosLosMensajes);
        
        enviar(chatService.generationStream(prompt, chatClient));
	}

	public void verMensaje() {
		mensaje = message.getRequest().getText();
		htmlContent = JsfHelper.markdown2Html(message.getResponse().getText());
	}
	
	@Override
	protected void processResponse() {
		assistantMessage = new AssistantMessage(respuesta.toString());
	}
	
	@Override
	protected void postCheckUpdate() {
		chatMemory.add(conversationId, List.of(userMessage, assistantMessage));
		rebuildHistorial();
	}
	
	private void rebuildHistorial() {
		historial.clear();
		historial.addAll(Optional.ofNullable(chatMemory)
		            .filter(cm -> StringUtils.isNotBlank(conversationId))
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
}