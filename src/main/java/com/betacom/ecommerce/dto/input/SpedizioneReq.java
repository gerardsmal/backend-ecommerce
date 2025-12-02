package com.betacom.ecommerce.dto.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class SpedizioneReq {
	private Integer id;
	private Boolean isDefault;
	private String nome;
	private String cognome;
	private String via;
	private String commune;
	private String cap;
	private Integer accountId;
}
