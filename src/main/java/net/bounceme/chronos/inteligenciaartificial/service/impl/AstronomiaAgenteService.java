package net.bounceme.chronos.inteligenciaartificial.service.impl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import net.bounceme.chronos.inteligenciaartificial.service.AgenteService;
import net.bounceme.chronos.inteligenciaartificial.tools.AstronomiaTools;

@Service
public class AstronomiaAgenteService implements AgenteService {

	@Autowired
	private ChatClient astronomiaAgenteChatClient;
	
	@Autowired
	private AstronomiaTools astronomiaTools;

	@PostConstruct
	private void initialize() {
		
	}
}
