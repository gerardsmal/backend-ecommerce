package com.betacom.ecommerce.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SigninDTO {

	private Integer userID;
	private String userName;
	private String role;
}
