package net.bounceme.chronos.inteligenciaartificial.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import net.bounceme.chronos.inteligenciaartificial.util.JsfUtils;

@Configuration
@Component
@Aspect
public class ShowTimeAspect {
	
	private transient TimeTraceAspect timeTraceAspect;

	public ShowTimeAspect(TimeTraceAspect timeTraceAspect) {
		this.timeTraceAspect = timeTraceAspect;
	}

	@After("net.bounceme.chronos.inteligenciaartificial.aspect.CommonJoinPointConfig.showTimeAnnotation()")
	public void after(ProceedingJoinPoint joinPoint) throws Throwable {
		JsfUtils.showDuration(timeTraceAspect.getTimeTaken());
	}
}
