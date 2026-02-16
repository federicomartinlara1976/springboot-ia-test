package net.bounceme.chronos.inteligenciaartificial.service.impl;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.aspect.annotations.LogTime;
import net.bounceme.chronos.inteligenciaartificial.model.ActorFilms;
import net.bounceme.chronos.inteligenciaartificial.model.FestivosPais;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
	
	private final ChatClient chatClient;
	
	private BeanOutputConverter<ActorFilms> actorFilmsOutputConverter;
	
	private BeanOutputConverter<FestivosPais> festivosPaisOutputConverter;
		    
	@Getter
	private ChatResponseMetadata chatResponseMetadata;

    public ChatServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
        actorFilmsOutputConverter = new BeanOutputConverter<>(ActorFilms.class);
        festivosPaisOutputConverter = new BeanOutputConverter<>(FestivosPais.class);
    }

	@Override
	@LogTime
	public String generation(String userInput) {
		ChatResponse chatResponse = chatClient.prompt()
	            .user(userInput)
	            .call()
	            .chatResponse();
		
		if (!Objects.isNull(chatResponse)) {
			chatResponseMetadata = chatResponse.getMetadata();
			log.info("Response metadata: {}", chatResponseMetadata.toString());
			
			return chatResponse.getResult().getOutput().getText();
		}
		
		return StringUtils.EMPTY;
	}

	@Override
	@LogTime
	public ActorFilms getActorFilms(String message) {
		return chatClient.prompt()
				.user(message)
				.call()
				.entity(ActorFilms.class);
	}

	@Override
	@LogTime
	public List<ActorFilms> getListActorFilms(String message) {
		return chatClient.prompt()
				.user(message)
			    .call()
			    .entity(new ParameterizedTypeReference<List<ActorFilms>>() {});
	}

	@Override
	@LogTime
	public ActorFilms getActorFilmsFormatted(String actor) {
		String template = """
		        Generate the filmography for {actor}.
		        {format}
		        """;
		
		return chatClient.prompt()
			.user(u -> u
				.text(template)
				.param("actor", actor)
				.param("format", actorFilmsOutputConverter.getFormat()))
			.call()
			.entity(ActorFilms.class);
	}

	@Override
	@LogTime
	public FestivosPais getFestivosPais(String pais, Integer year) {
		String template = """
		        Generar los festivos nacionales del país {pais} para el año {year}.
		        {format}
		        """;
		
		return chatClient.prompt()
				.user(u -> u
					.text(template)
					.param("pais", pais)
					.param("year", year)
					.param("format", festivosPaisOutputConverter.getFormat()))
				.call()
				.entity(FestivosPais.class);
	}

}
