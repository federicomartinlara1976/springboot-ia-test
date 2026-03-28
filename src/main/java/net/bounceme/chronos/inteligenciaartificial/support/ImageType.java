package net.bounceme.chronos.inteligenciaartificial.support;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ImageType {

	PNG("png", "image/png", MimeTypeUtils.IMAGE_PNG),
	JPG("jpg", "image/jpeg", MimeTypeUtils.IMAGE_JPEG),
	GIF("png", "image/gif", MimeTypeUtils.IMAGE_GIF),
	OCTECT_STREAM("", "application/octet-stream", MimeTypeUtils.APPLICATION_OCTET_STREAM);
	
	@Getter
	private String extension;
	
	@Getter
	private String type;
	
	@Getter
	private MimeType mimeType;
	
	public static ImageType getByExtension(String extension) {
		return Optional.ofNullable(extension)
	            .filter(StringUtils::isNotBlank)
	            .map(ext -> Arrays.stream(values())
	                    .filter(type -> type.getExtension().equals(ext))
	                    .findFirst()
	                    .orElse(null))
	            .orElse(ImageType.OCTECT_STREAM);
	}
}
