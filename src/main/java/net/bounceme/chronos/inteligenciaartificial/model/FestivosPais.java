package net.bounceme.chronos.inteligenciaartificial.model;

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
public class FestivosPais {
	
	@Getter
	@Setter
	private String pais;
	
	@Getter
	@Setter
	private List<Festivo> festivos;

}
