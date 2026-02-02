package net.bounceme.chronos.inteligenciaartificial.controller;

import org.springframework.stereotype.Component;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;
import net.bounceme.chronos.inteligenciaartificial.util.JsfUtils;

@Component
@Named
@RequestScoped
public class ChatBean {

	@Getter
	@Setter
	private String mensaje;
	
	@Getter
    private String htmlContent;
	
	private ChatService chatService;

	public ChatBean(ChatService chatService) {
		this.chatService = chatService;
	}
	
	public void enviar() {
		String respuesta = chatService.generation(mensaje);
		htmlContent = JsfUtils.markdown2Html(respuesta);
	}
}