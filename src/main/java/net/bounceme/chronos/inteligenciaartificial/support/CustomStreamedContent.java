package net.bounceme.chronos.inteligenciaartificial.support;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.primefaces.model.StreamedContent;
import org.primefaces.util.Callbacks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomStreamedContent implements StreamedContent, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private Callbacks.SerializableSupplier<InputStream> stream;
    
	@Getter
	private String contentType;
    
    @Getter
    private String name;
    
    @Getter
    private String contentEncoding;
    
    @Getter
    private Long contentLength;
    
    @Getter
    private Callbacks.SerializableConsumer<OutputStream> writer;

}
