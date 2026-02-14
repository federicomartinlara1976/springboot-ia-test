package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import net.bounceme.chronos.inteligenciaartificial.aspect.TimeTraceAspect;
import net.bounceme.chronos.inteligenciaartificial.model.ActorFilms;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;
import net.bounceme.chronos.inteligenciaartificial.util.Constants;
import net.bounceme.chronos.inteligenciaartificial.util.JsfUtils;

@Component
@Named
@ViewScoped
public class TestListaEntidadesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	private String mensaje = "Generate the filmography of 5 movies for 5 random actors.";
	
	@Getter
	@Setter
    private ActorFilms actorFilms;
	
	@Getter
	private List<ActorFilms> listActorFilms;
	
	private transient ChatService chatService;

	private transient TimeTraceAspect timeTraceAspect;

	public TestListaEntidadesBean(ChatService chatService, TimeTraceAspect timeTraceAspect) {
		this.chatService = chatService;
		this.timeTraceAspect = timeTraceAspect;
	}
	
	@PostConstruct
	private void init() {
		request();
	}

	public void request() {
		listActorFilms = chatService.getListActorFilms(mensaje);
		
		String sEllapsedTime = String.format(Constants.DURATION_FORMAT, timeTraceAspect.getTimeTaken());
		JsfUtils.writeMessage(FacesMessage.SEVERITY_INFO, "Duración", sEllapsedTime);
	}
}