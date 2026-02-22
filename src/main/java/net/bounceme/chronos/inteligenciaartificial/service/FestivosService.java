package net.bounceme.chronos.inteligenciaartificial.service;

import net.bounceme.chronos.inteligenciaartificial.model.FestivosPais;

public interface FestivosService {

	FestivosPais getFestivosPais(String pais, Integer year);
}
