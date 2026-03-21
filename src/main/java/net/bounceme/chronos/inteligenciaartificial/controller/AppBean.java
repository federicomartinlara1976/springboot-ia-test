package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
	
	public StreamedContent getImage() {
		FacesContext context = FacesContext.getCurrentInstance();
        String filePath = context.getExternalContext().getRequestParameterMap().get("file");
		
		if (StringUtils.isBlank(filePath)) {
	        return null; // o devolver una imagen por defecto
	    }
		
        return DefaultStreamedContent.builder()
                .contentType(getContentType(filePath))
                .stream(() -> {
                	try {
                        return new FileInputStream(filePath);
                    } catch (FileNotFoundException e) {
                        log.error("ERROR: ", e);
                        return null;
                    }
                })
                .build();
    }
	
	private String getContentType(String filePath) {
	    String extension = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();
	    switch (extension) {
	        case "png": return "image/png";
	        case "jpg", "jpeg": return "image/jpeg";
	        case "gif": return "image/gif";
	        default: return "application/octet-stream";
	    }
	}
}
