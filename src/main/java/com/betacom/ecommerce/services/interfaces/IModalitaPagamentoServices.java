package com.betacom.ecommerce.services.interfaces;

import java.util.List;

import com.betacom.ecommerce.dto.output.ModalitaPagamentoDTO;

public interface IModalitaPagamentoServices {
	List<ModalitaPagamentoDTO> list() throws Exception;
	ModalitaPagamentoDTO getModalita(Integer id) throws Exception;
}
