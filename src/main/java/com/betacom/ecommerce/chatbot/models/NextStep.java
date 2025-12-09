package com.betacom.ecommerce.chatbot.models;

import lombok.Data;

@Data
public class NextStep {

	   private String id;
	   private String condition;
	   private String label;
}
