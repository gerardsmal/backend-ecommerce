package com.betacom.ecommerce.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PrezzoDTO {
	private Integer id;
	private String supporto;
	private Double prezzo;
	private StockDTO stock;

}
