package net.bounceme.chronos.inteligenciaartificial.aspect.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import net.bounceme.chronos.inteligenciaartificial.util.JsfUtils;

@Configuration
@Aspect
public class ShowTimeAspect {
	
	private TimeTraceAspect timeTraceAspect;

	public ShowTimeAspect(TimeTraceAspect timeTraceAspect) {
		this.timeTraceAspect = timeTraceAspect;
	}

	@Around("net.bounceme.chronos.inteligenciaartificial.aspect.CommonJoinPointConfig.showTimeAnnotation()")
	public void after(ProceedingJoinPoint joinPoint) throws Throwable {
		joinPoint.proceed();
		JsfUtils.showDuration(timeTraceAspect.getTimeTaken());
	}
}
