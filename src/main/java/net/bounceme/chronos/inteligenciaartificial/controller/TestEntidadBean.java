package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import net.bounceme.chronos.inteligenciaartificial.aspect.TimeTraceAspect;
import net.bounceme.chronos.inteligenciaartificial.model.ActorFilms;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;
import net.bounceme.chronos.inteligenciaartificial.util.JsfUtils;

@Component
@Named
@ViewScoped
public class TestEntidadBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	private String mensaje = "Generate the filmography for a random actor.";
	
	@Getter
	@Setter
    private ActorFilms actorFilms;
	
	private transient ChatService chatService;

	private transient TimeTraceAspect timeTraceAspect;

	public TestEntidadBean(ChatService chatService, TimeTraceAspect timeTraceAspect) {
		this.chatService = chatService;
		this.timeTraceAspect = timeTraceAspect;
	}
	
	@PostConstruct
	private void init() {
		request();
	}
	
	public void request() {
		actorFilms = chatService.getActorFilms(mensaje);
		
		JsfUtils.showDuration(timeTraceAspect.getTimeTaken());
	}
}