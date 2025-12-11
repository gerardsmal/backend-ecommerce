package com.betacom.ecommerce.services.interfaces;

public interface IOrderCounterServices {

	Long nextOrderNumber() throws Exception;
	Long nextOrderProvisaryNumber() throws Exception; 
}
