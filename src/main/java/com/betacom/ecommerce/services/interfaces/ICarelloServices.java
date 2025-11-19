package com.betacom.ecommerce.services.interfaces;

import com.betacom.ecommerce.dto.input.CarelloReq;
import com.betacom.ecommerce.dto.input.RigaCarelloReq;

public interface ICarelloServices {

	void create(CarelloReq req) throws Exception;
	
	void addRiga(RigaCarelloReq req) throws Exception;
	void removeRiga(RigaCarelloReq req) throws Exception;
}
