package net.bounceme.chronos.inteligenciaartificial.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

import net.bounceme.chronos.inteligenciaartificial.dto.ConversationDTO;
import net.bounceme.chronos.inteligenciaartificial.dto.MessageDTO;
import reactor.core.publisher.Flux;

public interface ChatService {
	
	String generation(String userInput, ChatClient chatClient);
	
	Flux<ChatResponse> generationStream(Prompt prompt, ChatClient chatClient);
	
	ChatResponseMetadata getChatResponseMetadata();

	void save(ConversationDTO selectedConversation, List<MessageDTO> historial);
	
	List<ConversationDTO> getConversations();
}
