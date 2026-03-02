package net.bounceme.chronos.inteligenciaartificial.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.bounceme.chronos.inteligenciaartificial.model.Conversation;

public interface ConversationRepository extends MongoRepository<Conversation, String> {

}
