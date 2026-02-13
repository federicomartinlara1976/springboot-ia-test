package net.bounceme.chronos.inteligenciaartificial.service.impl;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.aspect.TrackTime;
import net.bounceme.chronos.inteligenciaartificial.model.ActorFilms;
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
	@TrackTime
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

	@Override
	@TrackTime
	public ActorFilms getActorFilms(String message) {
		return chatClient.prompt()
				.advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
				.user(message)
				.call()
				.entity(ActorFilms.class);
	}

	@Override
	@TrackTime
	public List<ActorFilms> getListActorFilms(String message) {
		return chatClient.prompt()
				.advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
			    .user(message)
			    .call()
			    .entity(new ParameterizedTypeReference<List<ActorFilms>>() {});
	}

}
