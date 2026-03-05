package net.bounceme.chronos.inteligenciaartificial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import net.bounceme.chronos.inteligenciaartificial.dto.ConversationDTO;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;
import net.bounceme.chronos.inteligenciaartificial.util.JsfHelper;

@Component
@Named
@SessionScoped
public class SessionBean {
	
	@Getter
	@Setter
	private ConversationDTO selectedConversation;

	@Autowired
	private ChatService chatService;
	
	public List<ConversationDTO> getConversations() {
		return chatService.getConversations();
	}
	
	public void deleteConversation() {
		chatService.deleteConversation(selectedConversation);
		JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Borrada", "Conversación borrada");
	}
}
