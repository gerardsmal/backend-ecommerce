package com.betacom.ecommerce.chatbot.models;

import java.util.List;

import lombok.Data;

@Data
public class DialogNode {
	  private String id;
	  private String question;
	  private String answer;
	  private List<NextStep> nextSteps;
}
