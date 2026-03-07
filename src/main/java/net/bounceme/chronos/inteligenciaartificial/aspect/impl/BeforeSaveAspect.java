package net.bounceme.chronos.inteligenciaartificial.aspect.impl;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import net.bounceme.chronos.inteligenciaartificial.model.Conversation;

@Configuration
@Aspect
@Slf4j
public class BeforeSaveAspect {

	@Around("net.bounceme.chronos.inteligenciaartificial.aspect.CommonJoinPointConfig.beforeSaveAnnotation()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		return joinPoint.proceed(processArgs(joinPoint.getArgs()));
	}

	private Object[] processArgs(Object[] args) {
		/**
		 * Para la operación de guardado save, sólo hay un argumento, por lo tanto
		 * sólo se recogerá el primero.
		 */
		Conversation conversation  = (Conversation) args[0];
		
		conversation.setUpdateTime(new Date());

		return args;
	}
}
