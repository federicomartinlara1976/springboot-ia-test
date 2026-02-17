package net.bounceme.chronos.inteligenciaartificial.aspect.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import net.bounceme.chronos.inteligenciaartificial.util.JsfHelper;

@Configuration
@Aspect
public class ShowTimeAspect {
	
	private LogTimeAspect timeTraceAspect;

	public ShowTimeAspect(LogTimeAspect timeTraceAspect) {
		this.timeTraceAspect = timeTraceAspect;
	}

	@Around("net.bounceme.chronos.inteligenciaartificial.aspect.CommonJoinPointConfig.showTimeAnnotation()")
	public void after(ProceedingJoinPoint joinPoint) throws Throwable {
		joinPoint.proceed();
		JsfHelper.showDuration(timeTraceAspect.getTimeTaken());
	}
}
