package com.betacom.ecommerce.exception;

public class EcommerceException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public EcommerceException() {
		super();
	}
	
	public EcommerceException(String message) {
		super(message);
	}
	
}
