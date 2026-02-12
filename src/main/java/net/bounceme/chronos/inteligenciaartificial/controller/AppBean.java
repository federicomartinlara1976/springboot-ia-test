package net.bounceme.chronos.inteligenciaartificial.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.Getter;

@Component
@Named
@ApplicationScoped
public class AppBean {

	@Getter
	@Value("${spring.application.name}")
	private String appName;
	
	@Getter
	@Value("${spring.application.version}")
	private String appVersion;
}
