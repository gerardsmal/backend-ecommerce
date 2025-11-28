package com.betacom.ecommerce.models;

import java.time.LocalDate;

import org.hibernate.annotations.Check;

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
@Table(name="riga_carello")
public class RigaCarello {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="data_creazione")
	private LocalDate dataCreazione;
	
	@Column (nullable = false)
	@Check(constraints = "quantita > 0")
	private Integer quantita;

	private Supporto supporto;

	
	@ManyToOne
	@JoinColumn (name="id_carello")	
	private Carello carello;
	
	@ManyToOne
	@JoinColumn (name="id_prodotto")
	private Prodotto prodotto;
	
}
