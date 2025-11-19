package com.betacom.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.ecommerce.models.Stock;

public interface IStockRepositoy extends JpaRepository<Stock, Integer>{

}
