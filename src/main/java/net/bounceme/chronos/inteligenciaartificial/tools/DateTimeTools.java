package net.bounceme.chronos.inteligenciaartificial.tools;

import java.time.LocalDateTime;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateTimeTools {

	@Tool(description = "Get the current date and time in the user's timezone")
    public String getCurrentDateTime() {
        log.info("getCurrentDateTime");
		return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
}
