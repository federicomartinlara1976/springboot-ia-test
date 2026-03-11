package net.bounceme.chronos.inteligenciaartificial.config;

import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuración de modelos de IA. Se hace en una clase separada
 * de la clase general por modularidad y mantenibilidad
 */
@Configuration
public class ChatModelsConfiguration {

	/**
	 * Proveedor para Mistral (Europa)
	 * 
	 * @param chatModel
	 * @return
	 */
	@Bean
	@Primary
    ChatClient mistralAiChatClient(MistralAiChatModel chatModel) {
        return buildChatClient(chatModel);
    }

    /**
     * Proveedor para Deepseek (China)
     * 
     * @param chatModel
     * @return
     */
    @Bean
    ChatClient deepseekChatClient(DeepSeekChatModel chatModel) {
        return buildChatClient(chatModel);
    }
    
    /**
     * Este es el constructor del chat
     * 
     * @param chatModel Proveedor del modelo de IA
     * @return
     */
    private static ChatClient buildChatClient(ChatModel chatModel) {
		return ChatClient.builder(chatModel)
        		.defaultAdvisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
        		.build();
	}
}
