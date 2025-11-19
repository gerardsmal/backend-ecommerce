package com.betacom.ecommerce.services.interfaces;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.betacom.ecommerce.dto.input.AccountReq;
import com.betacom.ecommerce.dto.input.SigninReq;
import com.betacom.ecommerce.dto.output.AccountDTO;
import com.betacom.ecommerce.dto.output.SigninDTO;

public interface IAccountServices {

	void create (AccountReq req) throws  Exception;
	void update (AccountReq req) throws  Exception;
	void delete (Integer id) throws  Exception;

	SigninDTO login(SigninReq req) throws Exception;
	
	List<AccountDTO> list(
			Integer id,
			String nome,
			String cognome,
			String commune,
			String status,
			String role)
			throws Exception;
}
