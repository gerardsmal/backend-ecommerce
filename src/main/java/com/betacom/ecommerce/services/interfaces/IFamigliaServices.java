package com.betacom.ecommerce.services.interfaces;

import java.util.List;

import com.betacom.ecommerce.dto.input.FamigliaReq;
import com.betacom.ecommerce.dto.output.FamigliaDTO;
import com.betacom.ecommerce.dto.output.ProdottoFamigliaDTO;

public interface IFamigliaServices {

	void create(FamigliaReq req) throws Exception;
	void update(FamigliaReq req) throws Exception;
	void delete(Integer id) throws Exception;

	List<FamigliaDTO> list(String pattern) throws Exception;
	List<ProdottoFamigliaDTO> listPerFamiglia() throws Exception;
	ProdottoFamigliaDTO ListByIdProdottoFamiglia(Integer id)  throws Exception;
}
