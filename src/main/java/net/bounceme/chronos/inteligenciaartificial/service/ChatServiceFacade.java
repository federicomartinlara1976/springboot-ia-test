package net.bounceme.chronos.inteligenciaartificial.service;

import org.springframework.ai.chat.client.ChatClient;

public interface ChatServiceFacade {

	ChatClient getChatClient(String chatModel);
}
