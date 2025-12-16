package com.betacom.ecommerce.chatbot.conversation;

import lombok.Data;

@Data
public class ConversationContext {
	private String sessionId;
    private String lastNodeId;
}
