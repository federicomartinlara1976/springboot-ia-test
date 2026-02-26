package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.StringUtils;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.bounceme.chronos.inteligenciaartificial.service.ChatService;
import net.bounceme.chronos.inteligenciaartificial.util.JsfHelper;
import reactor.core.Disposable;

@Component
@Named
@ViewScoped
public class ChatBean extends ChatSelectorBean implements Serializable {

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
	
	private volatile String respuesta = StringUtils.EMPTY;
	
	private transient Disposable subscription; // para poder cancelar si es necesario
	
	private volatile String status = "READY";
	
	private volatile boolean completionMessageShown = false;
	
	// Para guardar el último ChatResponse y extraer metadatos al final
    private transient AtomicReference<ChatResponse> lastChatResponse = new AtomicReference<>();

	public ChatBean(ChatService chatService) {
		this.chatService = chatService;
	}
	
	@SneakyThrows
	public void enviar() {
		// Cancelar suscripción anterior
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
		
		// Antes de enviar, limpiar la respuesta
		respuesta = StringUtils.EMPTY;
		status = "INICIADA";
		pollActive = true;
        completionMessageShown = false;
        lastChatResponse.set(null);
		
		subscription = chatService.generationStream(mensaje, chatClient)
				.doOnNext(chatResponse -> {
                    // Este código se ejecuta en el hilo reactivo por cada fragmento
                    String chunk = chatResponse.getResult().getOutput().getText();
                    respuesta += chunk;
                    htmlContent = JsfHelper.markdown2Html(respuesta);
                    
                    // Guardamos la última respuesta para usarla al final
                    lastChatResponse.set(chatResponse);
                })
                .doOnComplete(() -> {
                    // Al completarse, extraemos los metadatos de la última respuesta
                    ChatResponse finalResponse = lastChatResponse.get();
                    if (finalResponse != null) {
                        chatResponseMetadata = finalResponse.getMetadata();
                    }
                    status = "COMPLETADA";
                    pollActive = false;
                })
                .doOnError(error -> {
                    respuesta += "\n[Error: " + error.getMessage() + "]";
                    htmlContent = JsfHelper.markdown2Html(respuesta);
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
        	JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Completada", "Respuesta completada");
        } else if ("ERROR".equals(status) && !completionMessageShown) {
            completionMessageShown = true;
            JsfHelper.writeMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió un error en la generación");
        }
    }
    
    // Opcional: cancelar la suscripción al destruir la vista
    @PreDestroy
    public void cleanup() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }
}