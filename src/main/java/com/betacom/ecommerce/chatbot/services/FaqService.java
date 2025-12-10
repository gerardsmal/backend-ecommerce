package com.betacom.ecommerce.chatbot.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import java.util.Comparator;


import com.betacom.ecommerce.chatbot.repository.FaqRepository;
import com.betacom.ecommerce.chatbot.models.FaqEntry;
import lombok.RequiredArgsConstructor;

//  Dato un testo scritto dall’utente (userMessage), trovare la FAQ più simile fra quelle nel file faq_chatbot.json.
//    Se la trova:
//                restituisce Optional<FaqEntry> con dentro la FAQ migliore.
//    Se non trova niente di abbastanza simile:
//                 restituisce Optional.empty().
//
//  Poi sarà il ChatServiceImpl a decidere se usare questa risposta o dire “non so rispondere”.


@Service
@RequiredArgsConstructor
public class FaqService {

	private final FaqRepository faqRepository;
    private final SemanticMatcherService matcher;
    /**
     * Calcolo dello score per ogni FAQ
     */
    Optional<FaqEntry> findBestMatch(String userMessage) {
        return faqRepository.findAll().stream()
                .map(faq -> {
                    String combinedAlt = faq.getAltQuestions() != null
                            ? String.join(" ", faq.getAltQuestions())
                            : "";

                    String combinedKeywords = faq.getKeywords() != null
                            ? String.join(" ", faq.getKeywords())
                            : "";

                   /**
                    *  	Similarità globale su domanda + frasi alternative
                    *  globalSimilarity usa:
                    *  				Levenshtein → per gli errori di ortografia
                    *  				Jaccard token → per vedere quanto si assomigliano le parole
                    *               overlap keyword → quanto c’è in comune con le keywords
                    *               e ti restituisce uno score tra 0 e 1 (dove 1 = identico / perfetto).
                    *               
                    */
                    
   
                    double score = matcher.globalSimilarity(
                            userMessage,                             // ciò che ha scritto l’utente
                            faq.getQuestion() + " " + combinedAlt,  // domanda + varianti
                            combinedKeywords                        // parole chiave
                    );

                    return new FaqScored(faq, score);
                })         
                /**
                 * Scelta della FAQ migliore
                 */
                
                .max(Comparator.comparingDouble(FaqScored::score))  // Qui prende il FaqScored con lo score più alto.
                .filter(fs -> fs.score() > 0.5) //Se la FAQ migliore ha punteggio troppo basso (tipo 0.2, 0.3), 
                								// allora probabilmente l’utente stava chiedendo altro.
                .map(FaqScored::faq);
    }

    private record FaqScored(FaqEntry faq, double score) {}

}
