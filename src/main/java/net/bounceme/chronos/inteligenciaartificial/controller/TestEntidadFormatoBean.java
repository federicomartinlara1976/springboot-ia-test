package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.bounceme.chronos.inteligenciaartificial.aspect.annotations.ShowTime;
import net.bounceme.chronos.inteligenciaartificial.model.ActorFilms;
import net.bounceme.chronos.inteligenciaartificial.service.ActorFilmsService;

@Component
@Named
@ViewScoped
public class TestEntidadFormatoBean extends ChatSelectorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	private ActorFilms actorFilms;
	
	@Getter
	@Setter
	private String actor;
	
	private transient ActorFilmsService actorFilmsService;

	public TestEntidadFormatoBean(ActorFilmsService actorFilmsService) {
		this.actorFilmsService = actorFilmsService;
	}
	
	@ShowTime
	@SneakyThrows
	public void request() {
		actorFilms = actorFilmsService.getActorFilmsFormatted(actor, chatClient);
	}
}