package com.betacom.ecommerce.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseV1<T> {
	private T msg;
	
}
