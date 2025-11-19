package com.betacom.ecommerce.dto.output;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountDTO {

	private Integer id;
	private String nome;
	private String cognome;
	private String email;
	private String sesso; // true masc. false fem)
	private String telefono;
	private String via;
	private String commune;
	private String cap;
	private String userName;
	private String  role;
	private String status;  // true attivo false non attivo
	private LocalDate dataCreazione;
	private CarelloDTO carello;
}
