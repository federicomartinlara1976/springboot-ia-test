package net.bounceme.chronos.inteligenciaartificial.config;

import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestIAConfiguration {

	@Bean
	@Primary
    ChatClient mistralAiChatClient(MistralAiChatModel chatModel) {
        return buildChatClient(chatModel);
    }

    @Bean
    ChatClient deepseekChatClient(DeepSeekChatModel chatModel) {
        return buildChatClient(chatModel);
    }
    
    private ChatClient buildChatClient(ChatModel chatModel) {
		return ChatClient.builder(chatModel)
        		.defaultAdvisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)
        		.build();
	}
}
