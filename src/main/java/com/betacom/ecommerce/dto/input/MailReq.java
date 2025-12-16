package com.betacom.ecommerce.dto.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MailReq {
	private String to;
	private String oggetto;
	private String body;
	private String attachment;
}
