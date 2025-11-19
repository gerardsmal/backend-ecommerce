package com.betacom.ecommerce.services.interfaces;

import java.util.List;

import com.betacom.ecommerce.dto.input.ProdottoReq;
import com.betacom.ecommerce.dto.output.ProdottoDTO;

public interface IProdottoServices {
	
	Integer create(ProdottoReq req) throws Exception;
	void update(ProdottoReq req) throws Exception;
	void delete(Integer id) throws Exception;
	
	List<ProdottoDTO> list(Integer id, String desc, Integer artist, Integer famiglia) throws Exception;
	ProdottoDTO getById(Integer id ) throws Exception;
}
