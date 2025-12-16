package com.betacom.ecommerce.chatbot.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SynonymRepository {

	@Value("${chatbot.synonyms-file}")
	private Resource synonymsResource;

	private final ObjectMapper objectMapper;

	private Map<String, String> synonyms = Collections.emptyMap();

	@PostConstruct
	public void load() throws Exception {
		try (InputStream is = synonymsResource.getInputStream()) {
			synonyms = objectMapper.readValue(is, new TypeReference<Map<String, String>>() {
			});
		}
	}

	public Map<String, String> getSynonyms() {
		return synonyms;
	}
}
