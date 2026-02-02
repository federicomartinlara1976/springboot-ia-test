package net.bounceme.chronos.inteligenciaartificial.service.impl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import net.bounceme.chronos.inteligenciaartificial.service.ChatService;

@Service
public class ChatServiceImpl implements ChatService {
	
	private final ChatClient chatClient;

    public ChatServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

	@Override
	public String generation(String userInput) {
		return this.chatClient.prompt()
	            .user(userInput)
	            .call()
	            .content();
	}

}
