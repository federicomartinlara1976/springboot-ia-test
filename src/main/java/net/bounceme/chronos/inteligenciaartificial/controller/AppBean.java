package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.support.CustomStreamedContent;
import net.bounceme.chronos.inteligenciaartificial.util.AIUtils;

@Component
@Named
@ApplicationScoped
@Slf4j
public class AppBean {

	@Getter
	@Value("${spring.application.name}")
	private String appName;

	@Getter
	@Value("${spring.application.version}")
	private String appVersion;
	
	private StreamedContent streamedContent;
	
	public StreamedContent getImage() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		String parameter = context.getExternalContext().getRequestParameterMap().get("file");

		if (StringUtils.isNotBlank(parameter)) {	
			streamedContent = CustomStreamedContent.builder()
					.contentType(AIUtils.getContentType(parameter))
					.stream(() -> {
						try {
							log.info("Mostrar: {}", parameter);
							return new FileInputStream(parameter);
						} catch (FileNotFoundException e) {
							log.error("ERROR: ", e);
							return null;
						}
					})
					.build();
		}
		
		return streamedContent;
	}
}
