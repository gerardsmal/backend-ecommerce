package com.betacom.ecommerce.chatbot.repository;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import com.betacom.ecommerce.chatbot.models.DialogNode;

@Repository
@RequiredArgsConstructor
public class DialogRepository {

	@Value("${chatbot.flow-file}")
	private Resource flowResource;

	private final ObjectMapper objectMapper;
	private List<DialogNode> nodes = Collections.emptyList();

	/*
	 * load json flow file into nodes
	 */
	@PostConstruct
	public void load() throws Exception {
		try (InputStream is = flowResource.getInputStream()) {
			nodes = objectMapper.readValue(is, new TypeReference<List<DialogNode>>() {});
		}
	}

	public List<DialogNode> findAll() {
		return nodes;
	}
	
	/*
	 * search specific node
	 */
    public DialogNode findById(String id) {
        return nodes.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
