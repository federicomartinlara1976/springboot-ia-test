package net.bounceme.chronos.inteligenciaartificial.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SPRING_AI_CHAT_MEMORY")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {

	@EmbeddedId
	private MessageId id;
	
	private String content;
	
	private String type;
}
