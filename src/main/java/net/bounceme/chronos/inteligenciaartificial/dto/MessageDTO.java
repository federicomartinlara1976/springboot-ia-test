package net.bounceme.chronos.inteligenciaartificial.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;

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
	
	private String uuid;
	
	private String title;

	private Message request;
	
	private Message response;
	
	private ChatResponseMetadata responseMetadata;
	
	private Date requestTime;
	
	private Date responseTime;
}
