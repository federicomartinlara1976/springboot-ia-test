package net.bounceme.chronos.inteligenciaartificial.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "conversations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Conversation {

	@Id
    @Field("_id")
	@Getter
	@Setter
	private String conversationId;
	
	@Getter
	@Setter
	private String nombre;
	
	@Getter
	@Setter
	private Date fechaCreacion;
}
