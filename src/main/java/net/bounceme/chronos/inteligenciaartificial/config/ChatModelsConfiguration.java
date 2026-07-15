package net.bounceme.chronos.inteligenciaartificial.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import net.bounceme.chronos.inteligenciaartificial.util.ChatUtils;

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
        return ChatUtils.buildChatClient(chatModel);
    }

    /**
     * Proveedor para Deepseek (China)
     * 
     * @param chatModel
     * @return
     */
    @Bean
    ChatClient deepseekChatClient(DeepSeekChatModel chatModel) {
        return ChatUtils.buildChatClient(chatModel);
    }
    
    /**
     * Proveedor para agente específico y modelo específico
     * 
     * @param chatModel
     * @return
     */
    @Bean
	ChatClient astronomiaAgenteChatClient(MistralAiChatModel chatModel) {
        return ChatUtils.buildDefaultChatClient(chatModel, 
        		"""
        			Eres un asistente experto en astronomía. Tu objetivo es ayudar a los usuarios
                	a crear scripts de Octave para simulaciones y cálculos astronómicos.
                	Cuando necesites ejecutar código, usa la herramienta 'ejecutarOctave'.
                	Si el código falla, analiza el error, corrígelo y vuelve a intentarlo.
        		""");
    }
}
