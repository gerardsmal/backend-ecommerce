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
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="prodotto")
public class Prodotto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column (name="descrizione_prodotto",
			length=100,
			nullable = false)
	private String descrizione;
	
	
	@ManyToOne (optional = false)
	@JoinColumn (name="id_artista" , nullable = false)
	private Artist artista;
	
	@ManyToOne (optional = false)
	@JoinColumn (name="id_famiglia" , nullable = false)
	private Famiglia famiglia;
	
	@OneToMany(
			mappedBy = "prodotto",
			fetch = FetchType.EAGER,
			cascade = CascadeType.REMOVE)
	
	private List<Prezzo> prezzo;
	
	@OneToMany(
			mappedBy = "prodotto",
			fetch = FetchType.EAGER
			)
	private List<RigaCarello> rigaCarello;
}
