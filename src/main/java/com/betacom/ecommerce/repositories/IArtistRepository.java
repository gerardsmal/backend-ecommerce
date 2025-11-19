package com.betacom.ecommerce.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.betacom.ecommerce.models.Artist;

public interface IArtistRepository extends JpaRepository<Artist, Integer>{
	Optional<Artist> findByNome(String nome);
	boolean existsByNome(String nome);
	
	@Query(name="artist.searchByFilter")
	List<Artist> searchByFilter(@Param("nome") String nome, 
								@Param("famiglia")  Integer famiglia);
}
