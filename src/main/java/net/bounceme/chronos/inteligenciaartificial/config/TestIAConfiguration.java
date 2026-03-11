package net.bounceme.chronos.inteligenciaartificial.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.memory.repository.jdbc.MysqlChatMemoryRepositoryDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Configuración general de IA
 */
@Configuration
public class TestIAConfiguration {
    
    /**
     * La memoria del chat se obtiene de un medio persistente, una base de datos
     * 
     * @param jdbcTemplate
     * @return
     */
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
