package com.betacom.ecommerce.services.interfaces;

import com.betacom.ecommerce.dto.input.RigaCarelloReq;

public interface ICarelloServices {

	Integer create(RigaCarelloReq req) throws Exception;
	
	void addRiga(RigaCarelloReq req) throws Exception;
	void removeRiga(Integer id) throws Exception;
}
