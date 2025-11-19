package com.betacom.ecommerce.dto.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PrezzoReq {
	private Integer id;
	private Integer idProdotto;
	private String supporto;
	private Double prezzo;
	
	private Integer currentStock;  // update for stock
	private Integer stockAlert;

}
