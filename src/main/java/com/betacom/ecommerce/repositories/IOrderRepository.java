package com.betacom.ecommerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.betacom.ecommerce.enums.StatusPagamento;
import com.betacom.ecommerce.models.Account;
import com.betacom.ecommerce.models.Order;

public interface IOrderRepository extends JpaRepository<Order, Integer>{
	
	List<Order> findByAccountAndStatusPagamento(Account account, StatusPagamento status);
	
	@Query(name="order.searchByFilter")
	List<Order> searchByFilter(
			@Param("id") Integer id,
			@Param("productName") String productName,
			@Param("artist") String artist,
			@Param("genere") String genere);
}
