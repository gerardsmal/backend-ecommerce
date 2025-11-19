package com.betacom.ecommerce.dto.output;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class ProdottoDTO {
	private Integer id;
	private String descrizione;
	private FamigliaDTO famiglia;
	private ArtistaDTO artista;
	private List<PrezzoDTO> prezzo;
}
