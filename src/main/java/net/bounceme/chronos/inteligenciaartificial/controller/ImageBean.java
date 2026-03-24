package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Component;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.util.AIUtils;

@Component
@Named
@SessionScoped
@Slf4j
public class ImageBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Getter
    @Setter
	private String currentImagePath;

    public StreamedContent getImage() {
        if (currentImagePath == null) {
        	return null;
        }
        
        return DefaultStreamedContent.builder()
                .contentType(AIUtils.getContentType(currentImagePath))
                .stream(() -> {
                    try {
                    	log.info("Mostrar: {}", currentImagePath);
                        return new FileInputStream(currentImagePath);
                    } catch (FileNotFoundException e) {
                        return null;
                    }
                })
                .build();
    }
}
