package com.betacom.ecommerce.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SigninDTO {

	private Integer userID;
	private Integer carrelloSize;
	private Integer orderSize;
	private String userName;
	private String role;
	private Boolean validate;
}
