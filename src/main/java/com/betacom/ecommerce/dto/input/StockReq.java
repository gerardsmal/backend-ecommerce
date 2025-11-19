package com.betacom.ecommerce.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StockReq {
	private Integer prezzoId;
	private Integer currentStock;
	private Integer stockAlert;

}
