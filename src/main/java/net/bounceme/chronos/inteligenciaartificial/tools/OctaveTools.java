package net.bounceme.chronos.inteligenciaartificial.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import net.bounceme.chronos.inteligenciaartificial.service.CalcService;

@Component
public class OctaveTools {

	@Autowired
	@Qualifier("javaOctaveService")
	private transient CalcService calcService;
}
