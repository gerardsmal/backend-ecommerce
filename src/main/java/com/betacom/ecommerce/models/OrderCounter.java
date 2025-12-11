package com.betacom.ecommerce.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="contatore_ordine")
public class OrderCounter {
	@Id
	private Integer id = 1;
	
	@Column (name="current_value",
			nullable = true)
	private Long currentValue;

	@Column (name="provisary_value",
			nullable = true)
	private Long provisaryValue;

}
