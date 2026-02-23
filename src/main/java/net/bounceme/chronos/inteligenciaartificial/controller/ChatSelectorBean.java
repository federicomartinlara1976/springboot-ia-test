package net.bounceme.chronos.inteligenciaartificial.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;
import net.bounceme.chronos.inteligenciaartificial.service.ChatServiceSelector;

public class ChatSelectorBean {
	
	protected transient ChatClient chatClient;
	
	@Autowired
	private ChatServiceSelector chatServiceSelector;

	@Getter
	@Setter
	private String selectedChatModel;
	
	@Getter
	private List<String> options = Arrays.asList("mistralAiChatModel", "deepseekChatModel");
	
	public void modelChanged() {
		chatClient = chatServiceSelector.getChatClient(selectedChatModel);
	}
}
