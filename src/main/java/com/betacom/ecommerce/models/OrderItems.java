package com.betacom.ecommerce.models;

import java.time.LocalDate;

import com.betacom.ecommerce.enums.Supporto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="riga_ordine")
public class OrderItems {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="data_aggiunto_carella")
	private LocalDate dataCreazione;
	
	private Integer quantita;
	private String productName;
	private String artist;
	private String genere;
	private Supporto supporto;
	
	@Column(name="prezzo_unitatio")
	private double prezzoUnit;
	
	@Column(name="prezzo_da_pagare")
	private double prezzo;
	
	@ManyToOne
	@JoinColumn (name ="id_ordine")
	private Order order;
}
