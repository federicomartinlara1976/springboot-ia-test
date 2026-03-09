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
import net.bounceme.chronos.inteligenciaartificial.service.FestivosService;

@Component
@Named
@ViewScoped
public class TestListFestivosBean extends ChatSelectorBean implements Serializable {

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
	
	private transient FestivosService festivosService;

	public TestListFestivosBean(FestivosService festivosService) {
		this.festivosService = festivosService;
	}
	
	@ShowTime
	@SneakyThrows
	public void request() {
		festivosPais = festivosService.getFestivosPais(pais, year, chatClient);
	}
}