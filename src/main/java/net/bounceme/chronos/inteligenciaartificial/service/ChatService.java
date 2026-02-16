package net.bounceme.chronos.inteligenciaartificial.service;

import java.util.List;

import org.springframework.ai.chat.metadata.ChatResponseMetadata;

import net.bounceme.chronos.inteligenciaartificial.model.ActorFilms;
import net.bounceme.chronos.inteligenciaartificial.model.FestivosPais;

public interface ChatService {
	
	String generation(String userInput);
	
	ChatResponseMetadata getChatResponseMetadata();
	
	ActorFilms getActorFilms(String message);

	List<ActorFilms> getListActorFilms(String message);

	ActorFilms getActorFilmsFormatted(String actor);

	FestivosPais getFestivosPais(String pais, Integer year);
}
