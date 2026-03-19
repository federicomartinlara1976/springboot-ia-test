package net.bounceme.chronos.inteligenciaartificial.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.primefaces.model.file.UploadedFile;
import org.springframework.ai.chat.messages.Message;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.dto.MessageDTO;
import net.bounceme.chronos.inteligenciaartificial.model.ChatMessage;
import net.bounceme.chronos.utils.io.IOUtils;

@UtilityClass
@Slf4j
public class AIUtils {
	
	public String ellipsis(String inString, Integer limit) {
		if (inString.length() > limit) {
			String subString = inString.substring(0, limit);
			return subString + "...";
		}
		
		return inString;
	}
	
	public List<MessageDTO> convertirAParesDTO(List<Message> messages) {
	    List<ChatMessage> chatMessages = messages.stream()
	            .map(msg ->  new ChatMessage(UUID.randomUUID().toString(), 
	                                      msg.getMessageType(), 
	                                      msg.getText()))
	            .toList();

	    return IntStream.range(0, chatMessages.size() / 2)
	            .mapToObj(i -> crearDTO(chatMessages.get(i * 2), chatMessages.get(i * 2 + 1)))
	            .toList();
	}
	
	@SneakyThrows(IOException.class)
	public String createTempFile(UploadedFile source) {
		// FIXME - Sonar security issue
		File tempFile = File.createTempFile(source.getFileName(), ".tmp");
		
	    try (InputStream inputStream = source.getInputStream();
	    		OutputStream outputStream = new FileOutputStream(tempFile)) {
	    	IOUtils.copy(inputStream, outputStream, 8192);
	    } 
	    
	    return tempFile.getAbsolutePath();
	}
	
	@SneakyThrows(IOException.class)
	public byte[] readFile(String tempFile) {
		Path path = Paths.get(tempFile);
		return Files.readAllBytes(path);
	}
	
	@SneakyThrows(IOException.class)
	public void deleteTempFile(String filePath) {
		Path path = Paths.get(filePath);
		Files.delete(path);
	}
	
	private MessageDTO crearDTO(ChatMessage request, ChatMessage response) {
	    MessageDTO dto = new MessageDTO();
	    dto.setTitle(ellipsis(request.getText(), 40));
	    dto.setRequest(request);
	    dto.setResponse(response);
	    return dto;
	}
}
