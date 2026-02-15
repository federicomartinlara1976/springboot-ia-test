package net.bounceme.chronos.inteligenciaartificial.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
@JsonPropertyOrder({"actor", "movies"})
public class ActorFilms implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String actor;
	
	@Getter
	@Setter
	private List<String> movies;
}