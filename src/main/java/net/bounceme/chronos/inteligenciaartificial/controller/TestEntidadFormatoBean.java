package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.bounceme.chronos.inteligenciaartificial.aspect.annotations.ShowTime;
import net.bounceme.chronos.inteligenciaartificial.model.ActorFilms;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;

@Component
@Named
@ViewScoped
public class TestEntidadFormatoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	private ActorFilms actorFilms;
	
	@Getter
	@Setter
	@NotBlank(message = "Campo obligatorio")
	private String actor;
	
	private transient ChatService chatService;

	public TestEntidadFormatoBean(ChatService chatService) {
		this.chatService = chatService;
	}
	
	@ShowTime
	@SneakyThrows
	public void request() {
		actorFilms = chatService.getActorFilmsFormatted(actor);
	}
}