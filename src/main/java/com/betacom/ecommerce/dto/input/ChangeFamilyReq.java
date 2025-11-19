package com.betacom.ecommerce.dto.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChangeFamilyReq {
	private Integer id;
	private Integer IdFamiglia;
	private Integer newIdFamiglia;

}
