package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;
import net.bounceme.chronos.inteligenciaartificial.util.AIUtils;
import net.bounceme.chronos.inteligenciaartificial.util.JsfHelper;
import reactor.core.Disposable;

@Component
@Named
@ViewScoped
@Slf4j
public class ChatMultimodalBean extends ChatSelectorBean implements Serializable {

	private static final String ERROR = "Error";

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String mensaje;
	
	@Getter
    private volatile String htmlContent;
	
	@Getter 
	private volatile boolean pollActive = false;
	
	private transient ChatService chatService;
	
	private StringBuilder respuesta = new StringBuilder();
	
	private transient Disposable subscription; // para poder cancelar si es necesario
	
	@Getter
	private String status;
	
	private String tempFile;
	
	private volatile boolean completionMessageShown = false;
	
	// Para guardar el último ChatResponse y extraer metadatos al final
    private transient AtomicReference<ChatResponse> lastChatResponse;
    
    public ChatMultimodalBean(ChatService chatService) {
		this.chatService = chatService;
	}

	@PostConstruct
	private void init() {
		lastChatResponse = new AtomicReference<>();
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		try {
			tempFile = AIUtils.createTempFile(event.getFile());
			JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se ha subido la imagen.");
		} catch (Exception e) {
			log.error("ERROR: ", e);
			JsfHelper.writeMessage(FacesMessage.SEVERITY_ERROR, ERROR, "Ocurrió un error al subir la imagen.");
		}
    }
	
	@SneakyThrows
	public void enviar() {
		// 1. Antes de enviar, cancelar la suscripción anterior y limpiar la respuesta
		cancelSubscription();
		initProcess();
        
		AIUtils.getImageMedia(tempFile).ifPresentOrElse(imagen -> {
			// 2. Crear mensaje del usuario con la imagen
			UserMessage userMessage = UserMessage.builder()
    				.text(mensaje)
    				.media(imagen)
    				.build();
			
			// 3. Construir prompt
	        Prompt prompt = new Prompt(userMessage);
	        
	        subscription = chatService.generationStream(prompt, chatClient)
					.doOnNext(chatResponse -> {
	                    // Este código se ejecuta en el hilo reactivo por cada fragmento
	                    String chunk = chatResponse.getResult().getOutput().getText();
	                    respuesta.append(chunk);
	                    dumpResponse();
	                    
	                    // Guardamos la última respuesta para usarla al final
	                    lastChatResponse.set(chatResponse);
	                    status = "RUNNING";
	                })
	                .doOnComplete(() -> {
	                    status = "COMPLETADA";
	                    pollActive = false;
	                })
	                .doOnError(error -> {
	                    respuesta.append("\n[Error: " + error.getMessage() + "]");
	                    dumpResponse();
	                    status = "ERROR";
	                    pollActive = false;
	                })
	                .subscribe();
		}, () -> {
			status = "INICIADA";
            pollActive = false;
			JsfHelper.writeMessage(FacesMessage.SEVERITY_ERROR, ERROR, "No se ha cargado ninguna imagen");
		});
	}

	private void initProcess() {
		respuesta.setLength(0);
		status = "INICIADA";
		pollActive = true;
        completionMessageShown = false;
        lastChatResponse.set(null);
	}
	
	// Método que será llamado por el poll para "forzar" la actualización
    // No hace nada especial, solo sirve para que el poll ejecute una acción JSF
    public void checkUpdates() {
    	if ("COMPLETADA".equals(status) && !completionMessageShown) {
    		completionMessageShown = true;
    		
    		deleteTempFileIfExists();
    		
    		JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Completada", "Respuesta completada");
        } else if ("ERROR".equals(status) && !completionMessageShown) {
        	completionMessageShown = true;
        	
        	deleteTempFileIfExists();
    		
    		JsfHelper.writeMessage(FacesMessage.SEVERITY_ERROR, ERROR, "Ocurrió un error");
        }
    }

	private void deleteTempFileIfExists() {
		if (StringUtils.isNotBlank(tempFile)) {
			AIUtils.deleteTempFile(tempFile);
			tempFile = StringUtils.EMPTY;
		}
	}
    
    // Opcional: cancelar la suscripción al destruir la vista
    @PreDestroy
    public void cleanup() {
        cancelSubscription();
        deleteTempFileIfExists();
    }

	private void cancelSubscription() {
		if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
	}
	
	private void dumpResponse() {
		htmlContent = JsfHelper.markdown2Html(respuesta.toString());
	}
}