package net.bounceme.chronos.inteligenciaartificial.service;

import java.util.List;

import org.springframework.ai.chat.metadata.ChatResponseMetadata;

import net.bounceme.chronos.inteligenciaartificial.model.ActorFilms;

public interface ChatService {
	
	String generation(String userInput);
	
	ChatResponseMetadata getChatResponseMetadata();
	
	ActorFilms getActorFilms(String message);

	List<ActorFilms> getListActorFilms(String message);
}
