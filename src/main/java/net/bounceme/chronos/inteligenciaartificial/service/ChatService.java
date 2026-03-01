package net.bounceme.chronos.inteligenciaartificial.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

import reactor.core.publisher.Flux;

public interface ChatService {
	
	String generation(String userInput, ChatClient chatClient);
	
	Flux<ChatResponse> generationStream(Prompt prompt, ChatClient chatClient);
	
	ChatResponseMetadata getChatResponseMetadata();
}
