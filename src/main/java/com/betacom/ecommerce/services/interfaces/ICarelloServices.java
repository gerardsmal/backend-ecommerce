package com.betacom.ecommerce.services.interfaces;

import com.betacom.ecommerce.dto.input.RigaCarelloReq;

public interface ICarelloServices {

	Integer create(RigaCarelloReq req) throws Exception;
	Integer addRiga(RigaCarelloReq req) throws Exception;
	void updateQta(RigaCarelloReq req) throws Exception;
	
	void removeRiga(Integer id) throws Exception;
}
