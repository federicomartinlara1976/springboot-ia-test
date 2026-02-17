package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.SneakyThrows;
import net.bounceme.chronos.inteligenciaartificial.aspect.annotations.ShowTime;
import net.bounceme.chronos.inteligenciaartificial.model.ActorFilms;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;

@Component
@Named
@ViewScoped
public class TestEntidadBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	private String mensaje = "Generate the filmography for a random actor.";
	
	@Getter
	private ActorFilms actorFilms;
	
	private transient ChatService chatService;

	public TestEntidadBean(ChatService chatService) {
		this.chatService = chatService;
	}
	
	@ShowTime
	@SneakyThrows
	public void request() {
		actorFilms = chatService.getActorFilms(mensaje);
	}
}