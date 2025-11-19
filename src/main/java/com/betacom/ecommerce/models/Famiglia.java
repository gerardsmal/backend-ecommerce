package com.betacom.ecommerce.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table (name="famiglia")

public class Famiglia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column (nullable = false, length=100)
	private String descrizione;
	
	@OneToMany (
			mappedBy = "famiglia",
			fetch = FetchType.EAGER

			)
	private List<Prodotto> prodotto;
	
	@ManyToMany(
			mappedBy = "famiglia",
			fetch = FetchType.EAGER
			)
	private List<Artist> artist;
}
