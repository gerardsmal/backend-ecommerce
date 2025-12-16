package com.betacom.ecommerce.chatbot.services;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

import com.betacom.ecommerce.chatbot.repository.SynonymRepository;

import lombok.RequiredArgsConstructor;

import java.text.Normalizer;
import java.util.*;

// Questo servizio decide se la frase dell’utente corrisponde a una domanda conosciuta
//
// Serve a confrontare:
//    A) Ciò che scrive l’utente
//       es.:
//         “quali modaita di pagamnto accetate?”
//     
//     B) Ciò che è salvato nel tuo sistema
//				domande principali (question)
//				varianti (altQuestions)
//				parole chiave (keywords)
//				condizioni dei flussi (flow.json → nextSteps.condition)
//		
//		E restituisce un punteggio di similarità.
//           ➤ Più è alto il punteggio → più probabile che quella sia la risposta giusta.
//           ➤ Se il punteggio supera una soglia (es. 0.5) → il chatbot usa quella FAQ / nodo del flusso.

@Service
@RequiredArgsConstructor
public class SemanticMatcherService {


	    private final SynonymRepository synonymRepository;
	    private final LevenshteinDistance levenshtein = new LevenshteinDistance();

	    public String normalize(String text) {
	        if (text == null) return "";

	        String result = text.toLowerCase(Locale.ITALIAN);

	        result = Normalizer.normalize(result, Normalizer.Form.NFD)
	                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

	        result = result.replaceAll("[^a-z0-9\\s]", " ");

	        String[] tokens = result.split("\\s+");
	        List<String> mapped = new ArrayList<>();

	        Map<String, String> synonyms = synonymRepository.getSynonyms();

	        for (String t : tokens) {
	            if (t.isBlank()) continue;

	            String mappedToken = synonyms.getOrDefault(t, t);

	            mappedToken = stripSimplePlural(mappedToken);

	            mapped.add(mappedToken);
	        }
	        return String.join(" ", mapped).trim();
	    }

	    private String stripSimplePlural(String token) {
	        if (token.length() <= 4) return token;
	        if (token.endsWith("i") || token.endsWith("e")) {
	            return token.substring(0, token.length() - 1);
	        }
	        return token;
	    }

	    public List<String> tokenize(String text) {
	        String norm = normalize(text);
	        if (norm.isEmpty()) return List.of();
	        return Arrays.asList(norm.split("\\s+"));
	    }

	    public double levenshteinSimilarity(String a, String b) {
	        String na = normalize(a);
	        String nb = normalize(b);
	        if (na.isEmpty() && nb.isEmpty()) return 1.0;
	        if (na.isEmpty() || nb.isEmpty()) return 0.0;

	        int distance = levenshtein.apply(na, nb);
	        int maxLen = Math.max(na.length(), nb.length());
	        return 1.0 - (double) distance / maxLen;
	    }

	    public double jaccardTokenSimilarity(String a, String b) {
	        Set<String> setA = new HashSet<>(tokenize(a));
	        Set<String> setB = new HashSet<>(tokenize(b));

	        if (setA.isEmpty() && setB.isEmpty()) return 1.0;
	        if (setA.isEmpty() || setB.isEmpty()) return 0.0;

	        Set<String> intersection = new HashSet<>(setA);
	        intersection.retainAll(setB);

	        Set<String> union = new HashSet<>(setA);
	        union.addAll(setB);

	        return (double) intersection.size() / union.size();
	    }

	    public double keywordOverlapScore(String text, String keywords) {
	        String nText = normalize(text);
	        String nKw = normalize(keywords);

	        Set<String> tokensText = new HashSet<>(Arrays.asList(nText.split("\\s+")));
	        Set<String> tokensKw = new HashSet<>(Arrays.asList(nKw.split("\\s+")));

	        if (tokensKw.isEmpty()) return 0.0;

	        int matches = 0;
	        for (String kw : tokensKw) {
	            if (tokensText.stream().anyMatch(t -> t.contains(kw) || kw.contains(t))) {
	                matches++;
	            }
	        }
	        return (double) matches / tokensKw.size();
	    }

	    public double globalSimilarity(String userText, String targetText, String keywords) {
	        double lev = levenshteinSimilarity(userText, targetText);
	        double jac = jaccardTokenSimilarity(userText, targetText);
	        double key = 0.0;

	        if (keywords != null && !keywords.isBlank()) {
	            key = keywordOverlapScore(userText, keywords);
	        }

	        return 0.5 * lev + 0.3 * jac + 0.2 * key;
	    }
	}

