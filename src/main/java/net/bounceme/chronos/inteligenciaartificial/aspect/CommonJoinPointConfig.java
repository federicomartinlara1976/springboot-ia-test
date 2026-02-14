package net.bounceme.chronos.inteligenciaartificial.aspect;

import org.aspectj.lang.annotation.Pointcut;

/**
 * @author federico
 *
 */
public class CommonJoinPointConfig {
	
	/**
	 * 
	 */
	@Pointcut("@annotation(net.bounceme.chronos.inteligenciaartificial.aspect.annotations.LogTime)")
	public void logTimeAnnotation(){}
	
	@Pointcut("@annotation(net.bounceme.chronos.inteligenciaartificial.aspect.annotations.ShowTime)")
	public void showTimeAnnotation(){}

}
