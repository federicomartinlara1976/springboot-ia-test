package net.bounceme.chronos.inteligenciaartificial.service.impl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.aspect.annotations.LogTime;
import net.bounceme.chronos.inteligenciaartificial.model.FestivosPais;
import net.bounceme.chronos.inteligenciaartificial.service.FestivosService;

@Service
@Slf4j
public class FestivosServiceImpl implements FestivosService {
	
	public FestivosServiceImpl() {
		festivosPaisOutputConverter = new BeanOutputConverter<>(FestivosPais.class);
	}

	private BeanOutputConverter<FestivosPais> festivosPaisOutputConverter;
	
	@Override
	@LogTime
	public FestivosPais getFestivosPais(String pais, Integer year, ChatClient chatClient) {
		String template = """
				  Generar los festivos nacionales del país {pais} para el año {year}.
				  {format}
				""";

		return chatClient.prompt().user(u -> u.text(template).param("pais", pais).param("year", year).param("format",
				festivosPaisOutputConverter.getFormat())).call().entity(FestivosPais.class);
	}

}
