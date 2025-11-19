package com.betacom.ecommerce.dto.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ArtistReq {
	private Integer id;
	private String nome;
	private Integer IdFamiglia;

}
