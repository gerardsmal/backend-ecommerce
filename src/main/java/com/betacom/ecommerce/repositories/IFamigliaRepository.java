package com.betacom.ecommerce.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.ecommerce.models.Famiglia;

public interface IFamigliaRepository extends JpaRepository<Famiglia, Integer>{
	Optional<Famiglia> findByDescrizione(String descrizione);
	boolean existsByDescrizioneIgnoreCase(String descrizione);
	
	List<Famiglia> findByDescrizioneContainingIgnoreCase(String pattern, Sort sort);  // like %pattern%
}
