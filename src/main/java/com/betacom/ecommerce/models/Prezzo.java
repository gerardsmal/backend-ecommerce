package com.betacom.ecommerce.models;

import com.betacom.ecommerce.enums.Supporto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table (name="prezzo_prodotto")
public class Prezzo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Supporto supporto;
	private Double prezzo;
	
	@ManyToOne
	@JoinColumn (name="id_prodotto")
	private Prodotto prodotto;
	
	@OneToOne (
			cascade = CascadeType.REMOVE)
	
	@JoinColumn(
			name="stock_id",
			referencedColumnName = "id"
			
			)
	private Stock stock;
}
