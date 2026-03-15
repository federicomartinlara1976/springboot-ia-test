package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
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
	@Setter
	private transient UploadedFile imagenSubida; // Para PrimeFaces
	
	@Getter
    private volatile String htmlContent;
	
	@Getter 
	private volatile boolean pollActive = false;
	
	private transient ChatService chatService;
	
	private StringBuilder respuesta = new StringBuilder();
	
	private transient Disposable subscription; // para poder cancelar si es necesario
	
	@Getter
	private String status;
	
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
		imagenSubida = event.getFile();
		JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Correcto", imagenSubida.getFileName() + " se ha subido.");
    }
	
	@SneakyThrows
	public void enviar() {
		// No se enviará mensaje a no ser que la imagen sea correcta
		if (!comprobarImagenSubida()) {
			return;
		}
		
		Media imageMedia = getImageMedia();
	    
	    cancelSubscription();
		
		// Antes de enviar, limpiar la respuesta
		initProcess();
        
        // 2. Crear mensaje del usuario
        UserMessage userMessage = UserMessage.builder()
        				.text(mensaje)
        				.media(imageMedia)
        				.build();
        
        // 3. Construir prompt con historial + nuevo mensaje
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
	}

	private Media getImageMedia() {
		byte[] imagenBytes = null;

	    // Si hay imagen subida, leer los bytes AHORA (dentro de la petición JSF)
	    try {
	    	imagenBytes = imagenSubida.getContent(); // Obtener bytes directamente
	    } catch (Exception e) {
            JsfHelper.writeMessage(FacesMessage.SEVERITY_ERROR, ERROR, 
                "No se pudo leer la imagen: " + e.getMessage());
	    }
	    
	    // Convertir el archivo subido a un Resource
        // Crear un ByteArrayResource (implementación de Resource de Spring)
        ByteArrayResource imageResource = new ByteArrayResource(imagenBytes) {
            @Override
            public String getFilename() {
                return ""; // Para mantener el nombre original
            }
        };
        
        return new Media(MimeTypeUtils.IMAGE_JPEG, imageResource); // Ajusta el MimeType según tu imagen
	}

	private void initProcess() {
		respuesta.setLength(0);
		status = "INICIADA";
		pollActive = true;
        completionMessageShown = false;
        lastChatResponse.set(null);
	}

	private boolean comprobarImagenSubida() {
		if (Objects.isNull(imagenSubida)) {
			JsfHelper.writeMessage(FacesMessage.SEVERITY_ERROR, ERROR, "No se ha cargado ninguna imagen");
			return false;
		}
		else {
			if (imagenSubida.getSize() == 0) {
				JsfHelper.writeMessage(FacesMessage.SEVERITY_ERROR, ERROR, "Error al cargar la imagen");
				return false;
			}
		}
		
		return true;
	}
	
	// Método que será llamado por el poll para "forzar" la actualización
    // No hace nada especial, solo sirve para que el poll ejecute una acción JSF
    public void checkUpdates() {
    	if ("COMPLETADA".equals(status) && !completionMessageShown) {
    		completionMessageShown = true;
    		
    		PrimeFaces.current().ajax().update("historial");
			JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Completada", "Respuesta completada");
        } else if ("ERROR".equals(status) && !completionMessageShown) {
        	completionMessageShown = true;
    		
    		PrimeFaces.current().ajax().update("historial");
            JsfHelper.writeMessage(FacesMessage.SEVERITY_ERROR, ERROR, "Ocurrió un error");
        }
    }
    
    // Opcional: cancelar la suscripción al destruir la vista
    @PreDestroy
    public void cleanup() {
        cancelSubscription();
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