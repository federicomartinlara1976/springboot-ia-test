package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.bounceme.chronos.inteligenciaartificial.aspect.annotations.ShowTime;
import net.bounceme.chronos.inteligenciaartificial.model.ActorFilms;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;

@Component
@Named
@ViewScoped
public class TestListaEntidadesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	private String mensaje = "Generate the filmography for 5 random actors.";
	
	@Getter
	@Setter
    private ActorFilms actorFilms;
	
	@Getter
	private List<ActorFilms> listActorFilms;
	
	private transient ChatService chatService;

	public TestListaEntidadesBean(ChatService chatService) {
		this.chatService = chatService;
	}

	@ShowTime
	@SneakyThrows
	public void request() {
		listActorFilms = chatService.getListActorFilms(mensaje);
	}
}