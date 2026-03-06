package net.bounceme.chronos.inteligenciaartificial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.bounceme.chronos.inteligenciaartificial.model.Message;
import net.bounceme.chronos.inteligenciaartificial.model.MessageId;

public interface MessageRepository extends JpaRepository<Message, MessageId> {
	
	@Query("SELECT m FROM Message m WHERE m.id.conversationId = :conversationId")
	List<Message> buscarPorConversationId(@Param("conversationId") String conversationId);
}
