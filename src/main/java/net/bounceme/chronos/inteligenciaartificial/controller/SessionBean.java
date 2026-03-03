package net.bounceme.chronos.inteligenciaartificial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import net.bounceme.chronos.inteligenciaartificial.dto.ConversationDTO;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;

@Component
@Named
@ApplicationScoped
public class SessionBean {

	@Autowired
	private transient ChatService chatService;
	
	public List<ConversationDTO> getConversations() {
		return chatService.getConversations();
	}
}
