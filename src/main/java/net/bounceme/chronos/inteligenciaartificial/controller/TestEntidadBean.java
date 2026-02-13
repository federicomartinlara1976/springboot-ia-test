package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import net.bounceme.chronos.inteligenciaartificial.model.ActorFilms;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;

@Component
@Named
@ViewScoped
public class TestEntidadBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	private String mensaje1 = "Generate the filmography for a random actor.";
	
	@Getter
	private String mensaje2 = "Generate the filmography of 5 movies for 5 random actors.";
	
	@Getter
	@Setter
    private ActorFilms actorFilms;
	
	@Getter
	List<ActorFilms> listActorFilms;
	
	private transient ChatService chatService;

	public TestEntidadBean(ChatService chatService) {
		this.chatService = chatService;
	}
	
	@PostConstruct
	private void init() {
		actorFilms = chatService.getActorFilms(mensaje1); 
		listActorFilms = chatService.getListActorFilms(mensaje2);
	}
}