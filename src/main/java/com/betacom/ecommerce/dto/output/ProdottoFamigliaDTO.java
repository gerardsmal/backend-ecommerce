package com.betacom.ecommerce.dto.output;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
public class ProdottoFamigliaDTO {
	private Integer id;
	private String descrizione;
	List<ProdottoDTO> prodotto;
}
