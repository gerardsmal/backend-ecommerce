package com.betacom.ecommerce.services.interfaces;

import com.betacom.ecommerce.dto.input.PrezzoReq;

public interface IPrezzoServices {

	Integer addPrezzo(PrezzoReq req) throws Exception;
	void addPrezzoStock(PrezzoReq req) throws Exception;
	void removePrezzo(Integer id) throws Exception;
}
