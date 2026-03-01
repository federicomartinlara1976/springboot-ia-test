package net.bounceme.chronos.inteligenciaartificial.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Conversation {

	@Getter
	@Setter
	private String conversationId;
	
	@Getter
	@Setter
	private String nombre;
}
