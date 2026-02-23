package net.bounceme.chronos.inteligenciaartificial.service;

import org.springframework.ai.chat.client.ChatClient;

public interface ChatServiceSelector {

	ChatClient getChatClient(String chatModel);
}
