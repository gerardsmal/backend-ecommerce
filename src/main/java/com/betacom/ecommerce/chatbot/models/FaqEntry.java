package com.betacom.ecommerce.chatbot.models;

import java.util.List;

import lombok.Data;

@Data
public class FaqEntry {

	private String id;
	private String question;
    private List<String> altQuestions;
    private List<String> keywords;
    private String answer;
}
