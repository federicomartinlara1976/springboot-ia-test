package net.bounceme.chronos.inteligenciaartificial.service;

import org.springframework.ai.chat.client.ChatClient;

import net.bounceme.chronos.inteligenciaartificial.model.FestivosPais;

public interface FestivosService {

	FestivosPais getFestivosPais(String pais, Integer year, ChatClient chatClient);
}
