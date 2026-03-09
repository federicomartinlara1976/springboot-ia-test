package net.bounceme.chronos.inteligenciaartificial.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class MessageId implements Serializable {
    
	private static final long serialVersionUID = 1L;

	@Column(name="conversation_id",insertable=false, updatable=false)
	private String conversationId;
    
	private Date timestamp;
}
