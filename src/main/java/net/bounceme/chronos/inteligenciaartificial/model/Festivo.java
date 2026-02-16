package net.bounceme.chronos.inteligenciaartificial.model;

import java.io.Serializable;
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
public class Festivo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String nombre;
	
	@Getter
	@Setter
	private Date fecha;
}
