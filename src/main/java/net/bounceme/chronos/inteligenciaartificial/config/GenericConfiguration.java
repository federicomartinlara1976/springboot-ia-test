package net.bounceme.chronos.inteligenciaartificial.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class GenericConfiguration {

	@Bean
	@Scope("prototype")
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
