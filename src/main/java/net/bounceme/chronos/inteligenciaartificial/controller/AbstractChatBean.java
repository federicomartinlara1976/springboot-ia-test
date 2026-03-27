package net.bounceme.chronos.inteligenciaartificial.controller;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

import org.primefaces.PrimeFaces;
import org.springframework.ai.chat.model.ChatResponse;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.util.JsfHelper;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

@Slf4j
public abstract class AbstractChatBean extends ChatSelectorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	protected String mensaje;
	
	@Getter
    protected volatile String htmlContent;
	
	@Getter 
	protected volatile boolean pollActive = false;
	
	private StringBuilder respuesta = new StringBuilder();
	
	private transient Disposable subscription; // para poder cancelar si es necesario
	
	@Getter
	protected String status;
	
	private volatile boolean completionMessageShown = false;
	
	// Para guardar el último ChatResponse y extraer metadatos al final
    private transient AtomicReference<ChatResponse> lastChatResponse;

	@PostConstruct
	protected void init() {
		log.info("AbstractChatBean init");
		lastChatResponse = new AtomicReference<>();
	}
	
	@SneakyThrows
	protected void enviar(Flux<ChatResponse> suscriptor) {
		cancelSubscription();
		
		// Antes de enviar, limpiar la respuesta
		respuesta.setLength(0);
		status = "INICIADA";
		pollActive = true;
        completionMessageShown = false;
        lastChatResponse.set(null);
        
		subscription = suscriptor
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
                    
                    processResponse();
                })
                .doOnError(error -> {
                    respuesta.append("\n[Error: " + error.getMessage() + "]");
                    dumpResponse();
                    status = "ERROR";
                    pollActive = false;
                    
                    processResponse();
                })
                .subscribe();
	}
	
	// Método que será llamado por el poll para "forzar" la actualización
    // No hace nada especial, solo sirve para que el poll ejecute una acción JSF
    public void checkUpdates() {
    	if ("COMPLETADA".equals(status) && !completionMessageShown) {
    		completionMessageShown = true;
    		postCheckUpdate();
    		
    		PrimeFaces.current().ajax().update("historial");
			JsfHelper.writeMessage(FacesMessage.SEVERITY_INFO, "Completada", "Respuesta completada");
        } else if ("ERROR".equals(status) && !completionMessageShown) {
        	completionMessageShown = true;
        	postCheckUpdate();
    		
    		PrimeFaces.current().ajax().update("historial");
            JsfHelper.writeMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió un error");
        }
    }
    
    protected abstract void processResponse();
    
    protected abstract void postCheckUpdate();
    
    // Opcional: cancelar la suscripción al destruir la vista
    @PreDestroy
    public void cleanup() {
    	log.info("AbstractChatBean cleanup");
        cancelSubscription();
    }

	private void cancelSubscription() {
		if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
	}
	
	protected void dumpResponse() {
		htmlContent = JsfHelper.markdown2Html(respuesta.toString());
	}
}