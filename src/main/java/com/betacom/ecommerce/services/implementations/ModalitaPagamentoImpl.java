package com.betacom.ecommerce.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.ecommerce.dto.output.ModalitaPagamentoDTO;
import com.betacom.ecommerce.models.ModalitaPagamento;
import com.betacom.ecommerce.repositories.IModalidaPagamentoRepository;
import com.betacom.ecommerce.services.interfaces.IModalitaPagamentoServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class ModalitaPagamentoImpl implements IModalitaPagamentoServices{

	private final IModalidaPagamentoRepository pagaR;
	private final IValidationServices validS;

	@Override
	public List<ModalitaPagamentoDTO> list() throws Exception {
		log.debug("list");
		List<ModalitaPagamento> lM = pagaR.findAll();
		
		return lM.stream()
				.map (m -> ModalitaPagamentoDTO.builder()
						.id(m.getId())
						.tipo(m.getTipo())
						.build()
						).toList();
	}

	@Transactional (rollbackFor = Exception.class)
	@Override
	public ModalitaPagamentoDTO getModalita(Integer id) throws Exception {
		log.debug("getModalita: {}", id);
		ModalitaPagamento m = pagaR.findById(id)
				.orElseThrow(() -> new Exception(validS.getMessaggio("modalita_ntfnd")));
		return ModalitaPagamentoDTO.builder()
				.id(m.getId())
				.tipo(m.getTipo())
				.build();
				
	}

}
