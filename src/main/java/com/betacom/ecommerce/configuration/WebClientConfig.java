package com.betacom.ecommerce.configuration;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

@Configurable
public class WebClientConfig {

	@Bean
	WebClient webClient() {
	    return WebClient.builder()
	        .clientConnector(new JdkClientHttpConnector())
	        .build();
	}

}
