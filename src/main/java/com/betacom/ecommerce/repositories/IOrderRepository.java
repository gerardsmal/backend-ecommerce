package com.betacom.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.ecommerce.models.Order;

public interface IOrderRepository extends JpaRepository<Order, Integer>{

}
