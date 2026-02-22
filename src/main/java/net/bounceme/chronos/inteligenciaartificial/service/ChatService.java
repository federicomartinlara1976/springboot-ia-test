package net.bounceme.chronos.inteligenciaartificial.service;

import org.springframework.ai.chat.metadata.ChatResponseMetadata;

public interface ChatService {
	
	String generation(String userInput);
	
	ChatResponseMetadata getChatResponseMetadata();
}
