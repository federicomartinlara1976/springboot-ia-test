package net.bounceme.chronos.inteligenciaartificial.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class TomcatVirtualThreadConfig {

	@Bean
	TomcatConnectorCustomizer virtualThreadConnectorCustomizer() {
		return connector -> {
			// Configura el executor de Tomcat para usar Virtual Threads
            ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
            ProtocolHandler protocolHandler = connector.getProtocolHandler();
            
            if (protocolHandler instanceof AbstractProtocol<?> abstractProtocol) {
            	((AbstractProtocol<?>) protocolHandler).setExecutor(executorService);
            	log.info("Tomcat Executor: {}", abstractProtocol.getExecutor());
            }
		};
	}
	
	@Bean
	TomcatServletWebServerFactory tomcatFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		
		factory.addConnectorCustomizers(virtualThreadConnectorCustomizer());
		
		return factory;
	}
}
