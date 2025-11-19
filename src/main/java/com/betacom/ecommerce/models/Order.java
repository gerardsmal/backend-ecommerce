package com.betacom.ecommerce.models;

import java.time.LocalDate;
import java.util.List;

import com.betacom.ecommerce.enums.StatusPagamento;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="ordini")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="data_ordine")
	private LocalDate dataOrdine;

	@Column(name="data_invio")
	private LocalDate dataInvio;

	
	@Column (name="stato_pagamenti")
	private StatusPagamento statusPagamento;
	
	@Column (name="totale_ordine")
	private Double totale;
	
	@ManyToOne
	@JoinColumn (name="id_account")
	private Account account;
	
	@OneToMany(
			mappedBy = "order",
			cascade = CascadeType.REMOVE, orphanRemoval = true,
			fetch = FetchType.EAGER
			)
	private List<OrderItems> orderItems;
}
