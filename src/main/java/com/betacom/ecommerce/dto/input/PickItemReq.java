package com.betacom.ecommerce.dto.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class PickItemReq {
	private Integer prezzoId;
	private Integer numeroItems;

}
