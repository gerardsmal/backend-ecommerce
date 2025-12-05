package com.betacom.ecommerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.ecommerce.enums.StatusPagamento;
import com.betacom.ecommerce.models.Account;
import com.betacom.ecommerce.models.Order;

public interface IOrderRepository extends JpaRepository<Order, Integer>{
	
	List<Order> findByAccountAndStatusPagamento(Account account, StatusPagamento status);
}
