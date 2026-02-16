package net.bounceme.chronos.inteligenciaartificial.model;

import java.util.Date;

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
@JsonPropertyOrder({"nombre", "fecha"})
public class Festivo {
	
	@Getter
	@Setter
	private String nombre;
	
	@Getter
	@Setter
	private Date fecha;
}
