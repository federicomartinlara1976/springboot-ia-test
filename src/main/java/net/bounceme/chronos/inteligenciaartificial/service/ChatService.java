package net.bounceme.chronos.inteligenciaartificial.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;

public interface ChatService {
	
	String generation(String userInput, ChatClient chatClient);
	
	ChatResponseMetadata getChatResponseMetadata();
}
