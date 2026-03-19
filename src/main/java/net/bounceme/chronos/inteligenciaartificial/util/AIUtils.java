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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.file.UploadedFile;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.MimeTypeUtils;

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
		// 1. Asegurar directorio
	    String baseDir = System.getProperty("java.io.tmpdir");
	    File appTempDir = new File(baseDir, "miapp-uploads");
	    if (!appTempDir.exists()) {
	        appTempDir.mkdirs();
	    }
	    
	    // 2. Generar nombre seguro
	    String originalName = source.getFileName();
	    String extension = StringUtils.EMPTY;
	    int dotIndex = originalName.lastIndexOf('.');
	    if (dotIndex > 0) {
	        extension = originalName.substring(dotIndex);
	    }
	    String safeName = UUID.randomUUID().toString() + extension;
	    
	    // 3. Crear archivo y copiar
	    File tempFile = new File(appTempDir, safeName);
	    try (InputStream in = source.getInputStream();
	         OutputStream out = new FileOutputStream(tempFile)) {
	        IOUtils.copy(in, out, 8192);
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
	
	public Optional<Media> getImageMedia(String tempFile) {
		// Si hay imagen subida, leer los bytes AHORA (dentro de la petición JSF)
	    try {
	    	byte[] imagenBytes = readFile(tempFile);// Obtener bytes directamente
	    	
	    	// Convertir el archivo subido a un Resource
	        // Crear un ByteArrayResource (implementación de Resource de Spring)
	        ByteArrayResource imageResource = new ByteArrayResource(imagenBytes) {
	            @Override
	            public String getFilename() {
	                return tempFile; // Para mantener el nombre original
	            }
	        };
	        
	        return Optional.of(new Media(MimeTypeUtils.IMAGE_JPEG, imageResource)); // Ajusta el MimeType según tu imagen
	    } catch (Exception e) {
            return Optional.empty();
	    }
	}
	
	private MessageDTO crearDTO(ChatMessage request, ChatMessage response) {
	    MessageDTO dto = new MessageDTO();
	    dto.setTitle(ellipsis(request.getText(), 40));
	    dto.setRequest(request);
	    dto.setResponse(response);
	    return dto;
	}
}
