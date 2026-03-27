package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;

import org.primefaces.event.FileUploadEvent;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.controller.api.AbstractChatBean;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;
import net.bounceme.chronos.inteligenciaartificial.util.AIUtils;
import net.bounceme.chronos.inteligenciaartificial.util.JsfHelper;

@Component
@Named
@ViewScoped
@Slf4j
public class ChatMultimodalBean extends AbstractChatBean implements Serializable {

	private static final String ERROR = "Error";

	private static final long serialVersionUID = 1L;
	
	private transient ChatService chatService;
	
	@Getter
	@Setter
	private String tempFile;
	
	@Getter
	private Boolean imagenCargada;
    
    private ImageBean imageBean;
    
    public ChatMultimodalBean(ImageBean imageBean, ChatService chatService) {
		this.imageBean = imageBean;
    	this.chatService = chatService;
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		try {
			tempFile = AIUtils.createTempFile(event.getFile());
			imageBean.setCurrentImagePath(tempFile); // Guardar en sesión
			log.info("Creado archivo: {}", tempFile);
			
			imagenCargada = true;
			JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se ha subido la imagen.");
		} catch (Exception e) {
			log.error("ERROR: ", e);
			JsfHelper.writeMessage(FacesMessage.SEVERITY_ERROR, ERROR, "Ocurrió un error al subir la imagen.");
		}
    }
	
	@SneakyThrows
	public void enviar() {
		AIUtils.getImageMedia(tempFile).ifPresentOrElse(imagen -> {
			// 1. Crear mensaje del usuario con la imagen
			UserMessage userMessage = UserMessage.builder()
    				.text(mensaje)
    				.media(imagen)
    				.build();
			
			// 2. Construir prompt
	        Prompt prompt = new Prompt(userMessage);
	        
	        enviar(chatService.generationStream(prompt, chatClient));
		}, () -> {
			status = "INICIADA";
            pollActive = false;
			JsfHelper.writeMessage(FacesMessage.SEVERITY_ERROR, ERROR, "No se ha cargado ninguna imagen");
		});
	}

    @Override
	protected void processResponse() {
		log.debug("processResponse not implemented for this bean");
	}

	@Override
	protected void postCheckUpdate() {
		log.debug("postCheckUpdate not implemented for this bean");
	}
}