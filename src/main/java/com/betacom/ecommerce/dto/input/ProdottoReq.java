package com.betacom.ecommerce.dto.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ProdottoReq {

	private Integer id;
	private String descrizione;
	private Integer idFamiglia;
	private Integer idArtist;

}
