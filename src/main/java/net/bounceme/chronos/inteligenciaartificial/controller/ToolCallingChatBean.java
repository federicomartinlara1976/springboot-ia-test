package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.controller.api.AbstractChatBean;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;

@Component
@Named
@ViewScoped
@Slf4j
public class ToolCallingChatBean extends AbstractChatBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private transient ChatService chatService;
	
    protected ToolCallingChatBean(ChatService chatService) {
		super();
		this.chatService = chatService;
	}
	
	@SneakyThrows
	public void enviar() {
		// 1. Crear mensaje del usuario
        UserMessage userMessage = new UserMessage(mensaje);
        
        Prompt prompt = new Prompt(userMessage);
        
        enviar(chatService.generationStreamWithTools(prompt, chatClient));
	}

	@Override
	protected void processResponse() {
		log.debug("processResponse not implemented for this bean");
	}

	@Override
	protected void postCheckUpdate() {
		log.debug("postCheckUpdate not implemented for this bean");
	}
    
}