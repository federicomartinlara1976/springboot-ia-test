package net.bounceme.chronos.inteligenciaartificial.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SPRING_AI_CONVERSATIONS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversation extends CamposControl implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "conversation_id")
	@Getter
	@Setter
	private String conversationId;
	
	@Column(name = "name")
	@Getter
	@Setter
	private String nombre;
}
