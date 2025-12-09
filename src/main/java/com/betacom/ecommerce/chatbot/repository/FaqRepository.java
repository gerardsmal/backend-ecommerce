package com.betacom.ecommerce.chatbot.repository;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import com.betacom.ecommerce.chatbot.models.FaqEntry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FaqRepository {

	 @Value("${chatbot.faq-file}")
	 private Resource faqResource;

	 private final ObjectMapper objectMapper;
	 private List<FaqEntry> faqs = Collections.emptyList();

	 /*
	  * load json into FaqEntry
	  */	  
	 @PostConstruct  	  	  
	 public void load() throws Exception {	    
		 try (InputStream is = faqResource.getInputStream()) {
	    	faqs = objectMapper.readValue(is, new TypeReference<List<FaqEntry>>() {});	        
	    }	    
	 }
	  
	  public List<FaqEntry> findAll() {	
		  return faqs;	  
	  }
}
