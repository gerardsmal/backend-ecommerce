package com.betacom.ecommerce.dto.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RigaCarelloReq {
	private Integer id;
	private Integer idCarello;
	private Integer IdProdotto;
	private String supporto;
	private Integer quantita;
}
