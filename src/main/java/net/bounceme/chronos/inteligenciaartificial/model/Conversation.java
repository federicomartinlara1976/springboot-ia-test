package net.bounceme.chronos.inteligenciaartificial.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "spring_ai_conversations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Conversation {

	@Id
	@Column(name = "conversation_id")
    private String conversationId;
	
	@Column(name = "name")
	private String nombre;
	
	@Column(name = "timestamp")
	private Date fechaCreacion;
}
