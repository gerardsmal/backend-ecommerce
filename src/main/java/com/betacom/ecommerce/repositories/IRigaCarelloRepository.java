package com.betacom.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.betacom.ecommerce.models.RigaCarello;

import jakarta.transaction.Transactional;

public interface IRigaCarelloRepository extends JpaRepository<RigaCarello, Integer>{
	
	@Modifying
	@Transactional
	@Query(name="carello.removeItems")
	void removeItems(@Param("carelloID") Integer carelloID);
}
