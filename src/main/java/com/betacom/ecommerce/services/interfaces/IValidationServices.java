package com.betacom.ecommerce.services.interfaces;

import java.util.List;

import com.betacom.ecommerce.enums.Supporto;
import com.betacom.ecommerce.exception.EcommerceException;
import com.betacom.ecommerce.models.Prezzo;

public interface IValidationServices {
	String getMessaggio(String code);
	
	void validateWithRegex(String value, String regex, String msgKey) throws EcommerceException; 
	void checkNotNull(Object value, String messageKey) throws Exception;
	void validatePassword(String password) throws EcommerceException;

	Prezzo searchSupporto(List<Prezzo> prezzi, Supporto sup) throws Exception;

}
