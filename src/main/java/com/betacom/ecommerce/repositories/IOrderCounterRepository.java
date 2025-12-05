package com.betacom.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.ecommerce.models.OrderCounter;

public interface IOrderCounterRepository extends JpaRepository<OrderCounter, Integer>{

}
