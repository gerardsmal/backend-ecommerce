package com.betacom.ecommerce.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.betacom.ecommerce.models.Prodotto;

@Repository
public interface IProdottoRepository extends JpaRepository<Prodotto, Integer>{
	
	Optional<Prodotto> findByDescrizione(String descrizione);
	
	@Query(name="prodotto.searchByDesc" )
	List<Prodotto> searchByDescrizione(@Param("desc")  String desc);
	
	@Query(name="prodotto.searchByFilter" )
	List<Prodotto> searchByFilter(
			@Param("id") Integer id,
			@Param("desc") String desc,
			@Param("artist") Integer artist,
			@Param("famiglia") Integer famiglia
			);
}
