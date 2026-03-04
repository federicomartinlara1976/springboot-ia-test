package net.bounceme.chronos.inteligenciaartificial.repository.impl;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import net.bounceme.chronos.inteligenciaartificial.repository.RepositoryCollectionCustom;

/**
 * @author federico
 *
 */
@Component("repositoryCollectionCustom")
public class RepositoryCollectionCustomImpl implements RepositoryCollectionCustom {

	@Getter
	@Setter
	private String collectionName;

}
