package net.bounceme.chronos.inteligenciaartificial.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Aspect
@Slf4j
public class TimeTraceAspect {

	@Around("net.bounceme.chronos.inteligenciaartificial.aspect.CommonJoinPointConfig.trackTimeAnnotation()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();

		Object returnProceed = joinPoint.proceed();

		long timeTaken = System.currentTimeMillis() - startTime;
		log.info("{} execution time: {} milliseconds", joinPoint.getSignature(), timeTaken);

		return returnProceed;
	}
}
