package com.betacom.ecommerce.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<T, R> {
	private T msg;
	private R result;
	
}
