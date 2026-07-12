package net.bounceme.chronos.inteligenciaartificial.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.service.CalcService;

@Component
@Slf4j
public class AstronomiaTools {

	@Autowired
	@Qualifier("javaOctaveService")
	private transient CalcService calcService;

	@PostConstruct
	private void initialize() {
		// Load the astronomy package
		calcService.execute("pkg load astronomia");
		
		// Test if load is correct recovering the package version
		calcService.execute("astronomia_version = astronomia_version()");
		String packageVersion = calcService.getString("astronomia_version");
		log.info("Astronomia version: {}", packageVersion);
	}
}
