package net.bounceme.chronos.inteligenciaartificial.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestIAConfiguration {

	@Bean
	@Primary
    public ChatClient openAiChatClient(MistralAiChatModel chatModel) {
        return ChatClient.create(chatModel);
    }

    @Bean
    public ChatClient anthropicChatClient(DeepSeekChatModel chatModel) {
        return ChatClient.create(chatModel);
    }
}
