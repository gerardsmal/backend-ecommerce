package com.betacom.ecommerce.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SpedizioneDTO {

	private Integer id;
	private Boolean isDefault;
	private String nome;
	private String cognome;
	private String via;
	private String commune;
	private String cap;

}
