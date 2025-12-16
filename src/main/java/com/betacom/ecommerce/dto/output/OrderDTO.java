package com.betacom.ecommerce.dto.output;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderDTO {
	private Integer id;
	private String numeroOrdine;
	private LocalDate dataOrdine;
	private LocalDate dataInvio;
	private String status;
	private double prezzoTotale;
	private SpedizioneDTO spedizione;
	private String modalitaPagamento;
	List<OrderItemDTO> riga;
}
