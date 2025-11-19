package com.betacom.ecommerce.dto.output;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArtistaDTO {

	private Integer id;
	private String nome;
	private List<FamigliaDTO> famiglia;
}
