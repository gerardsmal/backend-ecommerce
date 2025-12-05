package com.betacom.ecommerce.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name="modalita_pagamento")
public class ModalitaPagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column (name="tipo_pagamento",
			length = 40,
			nullable = false)
	private String tipo;
	
	@OneToMany(
			mappedBy = "modalitaPagamento",
			cascade = CascadeType.REMOVE, orphanRemoval = true,
			fetch = FetchType.EAGER
			)
	private List<Order> order;
}
