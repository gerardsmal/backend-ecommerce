package com.betacom.ecommerce.models;

import java.util.List;

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

@Setter
@Getter
@Entity
@Table (name="anagrafica_spedizione")
public class Spedizione {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Boolean predefinito;
	
	@Column (name="nome",
			  nullable = false,
			  length = 100)
	private String nome;
	
	@Column (name="cognome",
			  nullable = false,
			  length = 100)
	private String cognome;

	@Column (name="via",
			  nullable = false,
			  length = 100)
	private String via;
	
	@Column (name="commune",
			  nullable = false,
			  length = 100)
	private String commune;

	@Column (name="cap",
			  nullable = false,
			  length = 5)
	private String cap;

	@ManyToOne
	@JoinColumn (name ="id_account")
	private Account account;

	@OneToMany(
			mappedBy = "spedizione",
			cascade = CascadeType.REMOVE, orphanRemoval = true,
			fetch = FetchType.EAGER
			)
	private List<Order> order;
	
}
