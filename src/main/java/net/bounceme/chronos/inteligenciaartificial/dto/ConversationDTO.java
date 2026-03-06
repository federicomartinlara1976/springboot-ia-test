package net.bounceme.chronos.inteligenciaartificial.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConversationDTO {

	private String conversationId;
	
	private String nombre;
	
	private Date fechaCreacion;
}
