package net.bounceme.chronos.inteligenciaartificial.util;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.ai.chat.messages.Message;

import lombok.experimental.UtilityClass;
import net.bounceme.chronos.inteligenciaartificial.dto.MessageDTO;
import net.bounceme.chronos.inteligenciaartificial.model.ChatMessage;

@UtilityClass
public class AIUtils {
	
	public List<MessageDTO> convertirAParesDTO(List<Message> messages) {
        List<ChatMessage> chatMessages = messages.stream()
                .map(msg -> new ChatMessage(UUID.randomUUID().toString(), 
                                          msg.getMessageType(), 
                                          msg.getText()))
                .toList();

        return IntStream.range(0, chatMessages.size() / 2)
                .mapToObj(i -> crearDTO(chatMessages.get(i * 2), chatMessages.get(i * 2 + 1)))
                .collect(Collectors.toList());
    }
	
	public MessageDTO crearDTO(ChatMessage request, ChatMessage response) {
        MessageDTO dto = new MessageDTO();
        dto.setUuid(request.getUuid());
        dto.setTitle(ellipsis(request.getText(), 40));
        dto.setRequest(request);
        dto.setResponse(response);
        return dto;
    }
	
	public String ellipsis(String inString, Integer limit) {
		if (inString.length() > limit) {
			String subString = inString.substring(0, limit);
			return subString + "...";
		}
		
		return inString;
	}
}
