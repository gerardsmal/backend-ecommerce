package com.betacom.ecommerce.chatbot.conversation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class ConversationStore {

	private final Map<String, ConversationContext> sessions = new ConcurrentHashMap<>();

	public ConversationContext getOrCreate(String sessionId) {  
        return sessions.computeIfAbsent(sessionId, id -> {  // crea un valore se non esiste ( metodo di ConcurrentHashMap)
            ConversationContext ctx = new ConversationContext();
            ctx.setSessionId(id);
            ctx.setLastNodeId(null);
            return ctx;
        });
    }
	
	 public void update(ConversationContext ctx) {
		 sessions.put(ctx.getSessionId(), ctx);
	 }
}
