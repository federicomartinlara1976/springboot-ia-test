package net.bounceme.chronos.inteligenciaartificial.aspect.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Component
@Aspect
@Slf4j
public class LogTimeAspect {
	
	@Getter
	private Long timeTaken;

	@Around("net.bounceme.chronos.inteligenciaartificial.aspect.CommonJoinPointConfig.logTimeAnnotation()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();

		Object returnProceed = joinPoint.proceed();

		timeTaken = System.currentTimeMillis() - startTime;
		log.info("{} execution time: {} milliseconds", joinPoint.getSignature(), timeTaken);

		return returnProceed;
	}
}
