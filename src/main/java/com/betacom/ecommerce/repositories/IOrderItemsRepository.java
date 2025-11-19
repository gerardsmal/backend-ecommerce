package com.betacom.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.ecommerce.models.OrderItems;

public interface IOrderItemsRepository extends JpaRepository<OrderItems, Integer>{

}
