package net.bounceme.chronos.inteligenciaartificial.dto;

import java.io.Serializable;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "#{@repositoryCollectionCustom.getCollectionName()}")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @Field("_id")
	private String uuid;
	
	@Field("conversationId")
	private String conversationId;
	
	@Field("title")
	private String title;

	@Field("request")
	private transient Message request;
	
	@Field("response")
	private transient Message response;
	
	@Field("metadata")
	private transient ChatResponseMetadata responseMetadata;
	
	@Field("requestTime")
	private Long requestTime;
	
	@Field("responseTime")
	private Long responseTime;
	
	@Field("ellapsedTime")
	private Long ellapsedTime;
	
	@Field("estado")
	private String estado;
}
