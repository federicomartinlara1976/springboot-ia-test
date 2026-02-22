package net.bounceme.chronos.inteligenciaartificial.service.impl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.bounceme.chronos.inteligenciaartificial.service.ChatServiceFacade;

@Service
public class ChatServiceFacadeImpl implements ChatServiceFacade {

	@Autowired
	private ChatClient mistralAiChatModel;

	@Autowired
	private ChatClient deepseekChatModel;

	@Override
	public ChatClient getChatClient(String chatModel) {
		switch (chatModel) {
		case "mistralAiChatModel":
			return mistralAiChatModel;
		case "deepseekChatModel":
			return deepseekChatModel;
		default:
			return null;
		}
	}

}
