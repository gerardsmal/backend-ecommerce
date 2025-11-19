package com.betacom.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.ecommerce.models.MessageID;
import com.betacom.ecommerce.models.Messaggi;

public interface IMessaggiRepository extends JpaRepository<Messaggi, MessageID>{

}
