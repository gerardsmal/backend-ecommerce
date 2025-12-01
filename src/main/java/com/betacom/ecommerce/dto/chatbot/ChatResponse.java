package com.betacom.ecommerce.dto.chatbot;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
	private String sessionId;
	private String answer;
	private boolean fromKnowledgeBase;
	private String source;
	private String currentNodeId;
	private List<String> suggestions;
}
