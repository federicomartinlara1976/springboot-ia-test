package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.bounceme.chronos.inteligenciaartificial.aspect.annotations.ShowTime;
import net.bounceme.chronos.inteligenciaartificial.model.Festivo;
import net.bounceme.chronos.inteligenciaartificial.model.FestivosPais;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;

@Component
@Named
@ViewScoped
public class TestListFestivosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	private FestivosPais festivosPais;
	
	@Getter
	@Setter
	private Festivo festivo;
	
	@Getter
	@Setter
	private String pais;
	
	@Getter
	@Setter
	private Integer year;
	
	private transient ChatService chatService;

	public TestListFestivosBean(ChatService chatService) {
		this.chatService = chatService;
	}
	
	@ShowTime
	@SneakyThrows
	public void request() {
		festivosPais = chatService.getFestivosPais(pais, year);
	}
}