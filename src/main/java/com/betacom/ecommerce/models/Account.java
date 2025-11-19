package com.betacom.ecommerce.models;


import java.time.LocalDate;
import java.util.List;

import com.betacom.ecommerce.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column (name="nome_utente",
			  nullable = false,
			  length = 100)
	private String nome;
	
	@Column (name="cognome_utente",
			  nullable = false,
			  length = 100)
	private String cognome;


	@Column (name="email",
			  nullable = false,
			  length = 100)
	private String email;
	
	private Boolean sesso; // true masc. false fem)

	@Column (name="numero_telefono",
			  length = 15)
	private String telefono;
	
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

	@Column (name="username",
			  nullable = false,
			  length = 50,
			  unique = true)	
	private String userName;
	 
	@Column (name="password",
			  nullable = false,
			  length = 255)	
	private String pwd;
	
	private Role role;
	
	private Boolean status;  // true attivo false non attivo
	
	@Column (name="data_creazione")
	private LocalDate dataCreazione;
	
	@OneToOne (
			mappedBy = "account",
			cascade = CascadeType.REMOVE
			)
	private Carello carello;
	

	@OneToMany(
			mappedBy = "account",
			fetch = FetchType.EAGER
			)
	private List<Order> oders; 
	
	
	
}
