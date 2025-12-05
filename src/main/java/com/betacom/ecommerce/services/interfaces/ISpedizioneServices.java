package com.betacom.ecommerce.services.interfaces;

import java.util.List;

import com.betacom.ecommerce.dto.input.SpedizioneReq;
import com.betacom.ecommerce.dto.output.SpedizioneDTO;

public interface ISpedizioneServices {

	void init(SpedizioneReq req) throws Exception;
	void create(SpedizioneReq req) throws Exception;
	void update(SpedizioneReq req) throws Exception;
	void delete(Integer spedizioneID) throws Exception;
	
	List<SpedizioneDTO> list(Integer id) throws Exception;
	SpedizioneDTO getById(Integer spedizioneID) throws Exception;

}
