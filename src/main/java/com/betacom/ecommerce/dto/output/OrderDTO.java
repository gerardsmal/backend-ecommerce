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
	private LocalDate dataOrdine;
	private LocalDate dataInvio;
	private String status;
	private double prezzoTotale;
	List<OrderItemDTO> riga;
}
