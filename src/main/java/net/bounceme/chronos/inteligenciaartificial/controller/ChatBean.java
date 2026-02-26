package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
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
    private String htmlContent;
	
	@Getter
	private transient ChatResponseMetadata chatResponseMetadata;
	
	private transient ChatService chatService;
	
	private String respuesta = StringUtils.EMPTY;
	
	private Disposable subscription; // para poder cancelar si es necesario
	
	private String status = "READY";

	public ChatBean(ChatService chatService) {
		this.chatService = chatService;
	}
	
	@SneakyThrows
	public void enviar() {
		// Antes de enviar, limpiar la respuesta
		respuesta = StringUtils.EMPTY;
		status = "INICIADA";
		
		subscription = chatService.generationStream(mensaje, chatClient)
				.subscribe(
		                chunk -> {
		                    // ESTO SE EJECUTA CADA VEZ QUE LLEGA UN NUEVO FRAGMENTO
		                    respuesta += chunk;
		                    htmlContent = JsfHelper.markdown2Html(respuesta);
		                },
		                error -> {
		                    // Manejar error
		                    respuesta += "\\n[Error: " + error.getMessage() + "]";
		                    htmlContent = JsfHelper.markdown2Html(respuesta);
		                    status = "ERROR";
		                },
                        () -> {
                            // On complete: aquí podemos obtener metadatos si corresponde
                        	status = "COMPLETADA";
                            chatResponseMetadata = chatService.getChatResponseMetadata();
                        }
		            );
	}
	
	// Método que será llamado por el poll para "forzar" la actualización
    // No hace nada especial, solo sirve para que el poll ejecute una acción JSF
    public void checkUpdates() {
    	if ("COMPLETADA".equals(status)) {
			status = "READY";
        	JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Completada", "Respuesta completada");
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