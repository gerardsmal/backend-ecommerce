package com.betacom.ecommerce.dto.output;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArtistaWebDTO {

	private Integer id;
	private String nome;
	private String families;
}
