package com.betacom.ecommerce.dto.chatbot;

import lombok.Data;

@Data
public class ChatRequest {

	private String sessionId;
	private String message;
}
