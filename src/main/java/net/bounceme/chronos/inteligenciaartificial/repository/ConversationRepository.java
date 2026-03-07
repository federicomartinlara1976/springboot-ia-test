package net.bounceme.chronos.inteligenciaartificial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.bounceme.chronos.inteligenciaartificial.aspect.annotations.BeforeSave;
import net.bounceme.chronos.inteligenciaartificial.model.Conversation;

public interface ConversationRepository extends JpaRepository<Conversation, String> {

	@BeforeSave
    <S extends Conversation> S save(S entity);
}
