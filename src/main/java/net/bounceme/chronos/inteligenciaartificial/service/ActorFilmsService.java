package net.bounceme.chronos.inteligenciaartificial.service;

import java.util.List;

import net.bounceme.chronos.inteligenciaartificial.model.ActorFilms;

public interface ActorFilmsService {
	
	ActorFilms getActorFilms(String message);

	List<ActorFilms> getListActorFilms(String message);

	ActorFilms getActorFilmsFormatted(String actor);
}
