package net.bounceme.chronos.inteligenciaartificial.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.aspect.annotations.LogTime;
import net.bounceme.chronos.inteligenciaartificial.dto.ConversationDTO;
import net.bounceme.chronos.inteligenciaartificial.model.Conversation;
import net.bounceme.chronos.inteligenciaartificial.model.Message;
import net.bounceme.chronos.inteligenciaartificial.repository.ConversationRepository;
import net.bounceme.chronos.inteligenciaartificial.repository.MessageRepository;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

	@Getter
	private ChatResponseMetadata chatResponseMetadata;
	
	private ConversationRepository conversationRepository;
	
	private MessageRepository messageRepository;
	
	private ModelMapper modelMapper;
	
	public ChatServiceImpl(ConversationRepository conversationRepository, 
			MessageRepository messageRepository, ModelMapper modelMapper) {
		this.conversationRepository = conversationRepository;
		this.messageRepository = messageRepository;
		this.modelMapper = modelMapper;
	}

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
    public Flux<ChatResponse> generationStream(Prompt prompt, ChatClient chatClient) {
        return chatClient.prompt(prompt)
                .stream()
                .chatResponse(); // <--- ESTO DEVUELVE UN FLUX de chatResponse
    }

	@Override
	@Transactional
	public void save(ConversationDTO selectedConversation) {
	    Conversation conversation = conversationRepository
	            .findById(selectedConversation.getConversationId())
	            .orElseGet(() -> {
	                Conversation newConv = modelMapper.map(selectedConversation, Conversation.class);
	                newConv.setCreateTime(new Date());
	                return newConv;
	            });
	    conversationRepository.save(conversation);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ConversationDTO> getConversations() {
		return conversationRepository.findAll().stream()
				.map(conversation -> modelMapper.map(conversation, ConversationDTO.class))
				.toList();
	}

	@Override
	public Optional<ConversationDTO> getConversation(String id) {
		return conversationRepository.findById(id)
				.map(c -> modelMapper.map(c, ConversationDTO.class));
	}

	@Override
	@Transactional
	public void deleteConversation(ConversationDTO selectedConversation) {
		List<Message> messages = messageRepository.buscarPorConversationId(selectedConversation.getConversationId());
		
		messages.forEach(message -> messageRepository.delete(message));
		
		conversationRepository.deleteById(selectedConversation.getConversationId());
	}
}
