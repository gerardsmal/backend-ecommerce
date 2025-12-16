package com.betacom.ecommerce.dto.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class MailReq {
	private String to;
	private String oggetto;
	private String body;
	private byte[] attachment;
}
