package com.betacom.ecommerce.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.betacom.ecommerce.enums.Role;
import com.betacom.ecommerce.models.Account;
import com.betacom.ecommerce.models.Prodotto;

public interface IAccountRepository extends JpaRepository<Account, Integer>{
	Optional<Account> findByUserName(String user);
	boolean existsByUserName(String user);

	
	@Query(name="account.searchByFilter" )
	List<Account> searchByFilter(
			@Param("id") Integer id,
			@Param("nome") String nome,
			@Param("cognome") String cognome,
			@Param("commune") String commune,
			@Param("status") Boolean status,
			@Param("role") Role role
			);
}
