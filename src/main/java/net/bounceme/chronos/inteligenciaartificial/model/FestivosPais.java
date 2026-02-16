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
@JsonPropertyOrder({"pais", "festivos"})
public class FestivosPais implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String pais;
	
	@Getter
	@Setter
	private List<Festivo> festivos;

}
