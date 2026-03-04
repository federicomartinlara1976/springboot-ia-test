package net.bounceme.chronos.inteligenciaartificial.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.bounceme.chronos.inteligenciaartificial.dto.MessageDTO;

public interface MessageRepository extends MongoRepository<MessageDTO, String> {

}