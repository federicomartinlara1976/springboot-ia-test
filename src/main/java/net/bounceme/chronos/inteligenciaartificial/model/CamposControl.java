package net.bounceme.chronos.inteligenciaartificial.model;

import java.util.Date;

import jakarta.persistence.Column;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public abstract class CamposControl {
	
	@Column(name = "create_timestamp")
	@Getter
	@Setter
	private Date createTime;

	@Column(name = "update_timestamp")
	@Getter
	@Setter
	private Date updateTime;
}
