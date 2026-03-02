package net.bounceme.chronos.inteligenciaartificial.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "conversations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Conversation {

	@Id
    @Field("_id")
	private String conversationId;
	
	private String nombre;
	
	private Date fechaCreacion;
}
