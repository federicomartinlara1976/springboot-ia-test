package net.bounceme.chronos.inteligenciaartificial.config;

import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.MysqlChatMemoryRepositoryDialect;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

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
    
    @Bean
    ChatMemory chatMemory(JdbcTemplate jdbcTemplate) {
    	// 1. Crear el repositorio donde se guardan físicamente los mensajes
		ChatMemoryRepository repository = JdbcChatMemoryRepository.builder()
                .jdbcTemplate(jdbcTemplate)
                .dialect(new MysqlChatMemoryRepositoryDialect()) // Para MySQL
                .build();

		
		return MessageWindowChatMemory.builder()  // ← Sustituye a MessageChatMemoryChatHistory
	            .chatMemoryRepository(repository)
	            .build();
    }
}
