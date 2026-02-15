package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;

import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.stereotype.Component;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.bounceme.chronos.inteligenciaartificial.aspect.annotations.ShowTime;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;
import net.bounceme.chronos.inteligenciaartificial.util.JsfHelper;

@Component
@Named
@ViewScoped
public class ChatBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String mensaje;
	
	@Getter
    private String htmlContent;
	
	@Getter
	private transient ChatResponseMetadata chatResponseMetadata;
	
	private transient ChatService chatService;

	public ChatBean(ChatService chatService) {
		this.chatService = chatService;
	}
	
	@ShowTime
	@SneakyThrows
	public void enviar() {
		String respuesta = chatService.generation(mensaje);
		htmlContent = JsfHelper.markdown2Html(respuesta);
		chatResponseMetadata = chatService.getChatResponseMetadata();
	}
}