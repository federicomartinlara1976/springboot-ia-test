package net.bounceme.chronos.inteligenciaartificial.util;

import lombok.experimental.UtilityClass;
import net.bounceme.chronos.inteligenciaartificial.dto.MessageDTO;

@UtilityClass
public class AIUtils {
	
	public void markDTO(MessageDTO messageDTO) {
       messageDTO.setTitle(ellipsis(messageDTO.getRequest().getText(), 40));
    }
	
	public String ellipsis(String inString, Integer limit) {
		if (inString.length() > limit) {
			String subString = inString.substring(0, limit);
			return subString + "...";
		}
		
		return inString;
	}
}
