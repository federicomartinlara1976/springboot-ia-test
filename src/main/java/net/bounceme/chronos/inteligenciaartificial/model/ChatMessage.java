package net.bounceme.chronos.inteligenciaartificial.model;

import java.util.Map;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChatMessage implements Message {
	
	@Getter
	private MessageType messageType;
    
	@Getter
	private String text;

	@Override
	public Map<String, Object> getMetadata() {
		return null;
	}
}
