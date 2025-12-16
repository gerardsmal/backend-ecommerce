package com.betacom.ecommerce.services.interfaces;

public interface IOrderCounterServices {

	String nextOrderNumber(Integer anno) throws Exception;
	String nextOrderProvisaryNumber(Integer anno) throws Exception; 
}
