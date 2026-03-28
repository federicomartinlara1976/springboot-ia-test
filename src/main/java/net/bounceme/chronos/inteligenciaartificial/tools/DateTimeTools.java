package net.bounceme.chronos.inteligenciaartificial.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DateTimeTools {

	@Tool(description = "Obtener la hora actual en la zona horaria del usuario")
    public String getCurrentDateTime() {
		String currentDateTime = LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString(); 
        log.info("getCurrentDateTime: {}", currentDateTime);
        
		return currentDateTime;
    }
	
	@Tool(description = "Establece una alarma para la hora dada, proporcionada en formato ISO-8601")
    void setAlarm(String time) {
        LocalDateTime alarmTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        log.info("Alarm set for {}", alarmTime);
    }
}
