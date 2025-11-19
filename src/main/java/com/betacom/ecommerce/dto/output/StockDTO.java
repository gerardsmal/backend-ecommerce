package com.betacom.ecommerce.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class StockDTO {

	private Integer id;
	private Integer currentStock;
	private Integer stockAlert;
}
