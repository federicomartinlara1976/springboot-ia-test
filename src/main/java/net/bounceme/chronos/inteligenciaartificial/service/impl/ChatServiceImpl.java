package net.bounceme.chronos.inteligenciaartificial.service.impl;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.aspect.annotations.LogTime;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

	@Getter
	private ChatResponseMetadata chatResponseMetadata;

	@Override
	@LogTime
	public String generation(String userInput, ChatClient chatClient) {
		ChatResponse chatResponse = chatClient.prompt().user(userInput).call().chatResponse();

		if (!Objects.isNull(chatResponse)) {
			chatResponseMetadata = chatResponse.getMetadata();
			log.info("Response metadata: {}", chatResponseMetadata.toString());

			return chatResponse.getResult().getOutput().getText();
		}

		return StringUtils.EMPTY;
	}
	
	// Este método NO es bloqueante. Devuelve un Flux (un stream reactivo).
    public Flux<ChatResponse> generationStream(String userInput, ChatClient chatClient) {
        return chatClient.prompt()
                .user(userInput)
                .stream()
                .chatResponse(); // <--- ESTO DEVUELVE UN FLUX de chatResponse
    }
}
