package net.bounceme.chronos.inteligenciaartificial.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SPRING_AI_CONVERSATIONS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Conversation implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "conversation_id")
	private String conversationId;
	
	@Column(name = "name")
	private String nombre;
	
	@Column(name = "create_timestamp")
	private Date createTime;

	@Column(name = "update_timestamp")
	private Date updateTime;
}
