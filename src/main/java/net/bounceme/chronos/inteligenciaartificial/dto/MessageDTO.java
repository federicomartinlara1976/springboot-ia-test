package net.bounceme.chronos.inteligenciaartificial.dto;

import java.io.Serializable;

import org.springframework.ai.chat.messages.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String conversationId;
	
	private String title;

	private transient Message request;
	
	private transient Message response;
	
	private Long requestTime;
	
	private Long responseTime;
}
