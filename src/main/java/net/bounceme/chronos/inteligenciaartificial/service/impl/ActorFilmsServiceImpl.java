package net.bounceme.chronos.inteligenciaartificial.service.impl;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.aspect.annotations.LogTime;
import net.bounceme.chronos.inteligenciaartificial.model.ActorFilms;
import net.bounceme.chronos.inteligenciaartificial.service.ActorFilmsService;

@Service
@Slf4j
public class ActorFilmsServiceImpl implements ActorFilmsService {
	
	private final ChatClient chatClient;
	
	private BeanOutputConverter<ActorFilms> actorFilmsOutputConverter;

	public ActorFilmsServiceImpl(ChatClient chatClient) {
		this.chatClient = chatClient;
		actorFilmsOutputConverter = new BeanOutputConverter<>(ActorFilms.class);
	}

	@Override
	@LogTime
	public ActorFilms getActorFilms(String message) {
		return chatClient.prompt().user(message).call().entity(ActorFilms.class);
	}

	@Override
	@LogTime
	public List<ActorFilms> getListActorFilms(String message) {
		return chatClient.prompt().user(message).call().entity(new ParameterizedTypeReference<List<ActorFilms>>() {
		});
	}

	@Override
	@LogTime
	public ActorFilms getActorFilmsFormatted(String actor) {
		String template = """
				  Generate the filmography for {actor}.
				  {format}
				""";

		return chatClient.prompt().user(
				u -> u.text(template).param("actor", actor).param("format", actorFilmsOutputConverter.getFormat()))
				.call().entity(ActorFilms.class);
	}
}
