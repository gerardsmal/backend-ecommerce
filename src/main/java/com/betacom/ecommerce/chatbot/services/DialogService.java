package com.betacom.ecommerce.chatbot.services;

import java.util.Comparator;

import org.springframework.stereotype.Service;

import com.betacom.ecommerce.chatbot.models.DialogNode;
import com.betacom.ecommerce.chatbot.models.NextStep;
import com.betacom.ecommerce.chatbot.repository.DialogRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DialogService {
	private final DialogRepository dialogRepository;
	private final SemanticMatcherService matcher;

	public DialogNode findEntryNodeByUserMessage(String message) {
	    return dialogRepository.findAll().stream()
	            .max(Comparator.comparingDouble(node ->
	                    matcher.globalSimilarity(message, node.getQuestion(), null)))
	            .filter(node ->
	                    matcher.globalSimilarity(message, node.getQuestion(), null) > 0.5)
	            .orElse(null);
	}


	public DialogNode findNextNode(DialogNode current, String message) {

	    if (current.getNextSteps() == null || current.getNextSteps().isEmpty()) {
	        return null;
	    }

	    NextStep bestStep = current.getNextSteps().stream()
	            .max(Comparator.comparingDouble(step ->
	                    matcher.globalSimilarity(message, step.getCondition(), null)))
	            .filter(step ->
	                    matcher.globalSimilarity(message, step.getCondition(), null) > 0.5)
	            .orElse(null);

	    if (bestStep == null) {
	        return null;
	    }

	    return dialogRepository.findById(bestStep.getId());
	}
}
