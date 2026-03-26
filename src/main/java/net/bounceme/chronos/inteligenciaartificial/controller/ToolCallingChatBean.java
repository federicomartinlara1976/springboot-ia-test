package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

import org.primefaces.PrimeFaces;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

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
public class ToolCallingChatBean extends ChatSelectorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String mensaje;
	
	@Getter
    private volatile String htmlContent;
	
	@Getter
	private transient ChatResponseMetadata chatResponseMetadata;
	
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
    
	public ToolCallingChatBean(ChatService chatService) {
		this.chatService = chatService;
	}

	@PostConstruct
	private void init() {
		lastChatResponse = new AtomicReference<>();
	}
	
	@SneakyThrows
	public void enviar() {
		cancelSubscription();
		
		// Antes de enviar, limpiar la respuesta
		respuesta.setLength(0);
		status = "INICIADA";
		pollActive = true;
        completionMessageShown = false;
        lastChatResponse.set(null);
        
        // 1. Crear mensaje del usuario
        UserMessage userMessage = new UserMessage(mensaje);
        
        Prompt prompt = new Prompt(userMessage);
        
		subscription = chatService.generationStreamWithTools(prompt, chatClient)
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
            JsfHelper.writeMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió un error");
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