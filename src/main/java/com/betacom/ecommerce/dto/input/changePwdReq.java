package com.betacom.ecommerce.dto.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class changePwdReq {
	Integer id;
	String oldPwd;
	String newPwd;
}
