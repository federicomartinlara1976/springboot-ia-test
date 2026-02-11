package net.bounceme.chronos.inteligenciaartificial.service.impl;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
	
	private final ChatClient chatClient;
	
	@Getter
	private ChatResponseMetadata chatResponseMetadata;

    public ChatServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

	@Override
	public String generation(String userInput) {
		ChatResponse chatResponse = chatClient.prompt()
	            .user(userInput)
	            .call()
	            .chatResponse();
		
		if (!Objects.isNull(chatResponse)) {
			chatResponseMetadata = chatResponse.getMetadata();
			log.info("Response metadata: {}", chatResponseMetadata.toString());
			
			return chatResponse.getResult().getOutput().getText();
		}
		
		return StringUtils.EMPTY;
	}

}
