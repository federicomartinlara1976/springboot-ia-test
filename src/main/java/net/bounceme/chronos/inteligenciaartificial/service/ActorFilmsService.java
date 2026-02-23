package net.bounceme.chronos.inteligenciaartificial.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;

import net.bounceme.chronos.inteligenciaartificial.model.ActorFilms;

public interface ActorFilmsService {
	
	ActorFilms getActorFilms(String message, ChatClient chatClient);

	List<ActorFilms> getListActorFilms(String message, ChatClient chatClient);

	ActorFilms getActorFilmsFormatted(String actor, ChatClient chatClient);
}
