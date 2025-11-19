package com.betacom.ecommerce.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.ecommerce.enums.Supporto;
import com.betacom.ecommerce.models.Prezzo;

public interface IPrezzoRepository extends JpaRepository<Prezzo, Integer>{

	Optional<Prezzo> findBySupporto(Supporto supporto);
}
