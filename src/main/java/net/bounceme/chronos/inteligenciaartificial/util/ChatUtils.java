package net.bounceme.chronos.inteligenciaartificial.util;

import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ChatUtils {

	/**
     * Este es el constructor del chat
     * 
     * @param chatModel Proveedor del modelo de IA
     * @return
     */
    public ChatClient buildChatClient(ChatModel chatModel) {
		return ChatClient.builder(chatModel)
        		.defaultAdvisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
        		.build();
	}
    
    /**
     * Este es el constructor del chat
     * 
     * @param chatModel Proveedor del modelo de IA
     * @return
     */
    public ChatClient buildDefaultChatClient(ChatModel chatModel, String instruction) {
		return ChatClient.builder(chatModel)
				.defaultSystem(instruction)
        		.defaultAdvisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
        		.build();
	}
}
