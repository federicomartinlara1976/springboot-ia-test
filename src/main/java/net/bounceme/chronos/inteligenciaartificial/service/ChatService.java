package net.bounceme.chronos.inteligenciaartificial.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;

import reactor.core.publisher.Flux;

public interface ChatService {
	
	String generation(String userInput, ChatClient chatClient);
	
	Flux<String> generationStream(String userInput, ChatClient chatClient);
	
	ChatResponseMetadata getChatResponseMetadata();
}
