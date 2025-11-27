package com.betacom.ecommerce.services.implementations;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.ecommerce.dto.input.PickItemReq;
import com.betacom.ecommerce.dto.input.RigaCarelloReq;
import com.betacom.ecommerce.enums.StatoCarello;
import com.betacom.ecommerce.enums.Supporto;
import com.betacom.ecommerce.exception.EcommerceException;
import com.betacom.ecommerce.models.Account;
import com.betacom.ecommerce.models.Carello;
import com.betacom.ecommerce.models.Prezzo;
import com.betacom.ecommerce.models.Prodotto;
import com.betacom.ecommerce.models.RigaCarello;
import com.betacom.ecommerce.repositories.IAccountRepository;
import com.betacom.ecommerce.repositories.ICarelloRepository;
import com.betacom.ecommerce.repositories.IProdottoRepository;
import com.betacom.ecommerce.repositories.IRigaCarelloRepository;
import com.betacom.ecommerce.services.interfaces.ICarelloServices;
import com.betacom.ecommerce.services.interfaces.IStockServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CarelloImpl implements ICarelloServices{
	
	private ICarelloRepository carR;
	private IAccountRepository accountR;
	private IValidationServices  validS;
	private IProdottoRepository prodR;
	private IRigaCarelloRepository rigaR;
	private IStockServices stockS;
	
	public CarelloImpl(ICarelloRepository carR, 
			IAccountRepository accountR, 
			IValidationServices validS, 
			IProdottoRepository prodR,
			IRigaCarelloRepository rigaR,
			IStockServices stockS) {
		this.carR = carR;
		this.accountR = accountR;
		this.validS = validS;
		this.prodR = prodR;
		this.rigaR = rigaR;
		this.stockS = stockS;
	}
	
	@Transactional (rollbackFor = Exception.class)
	@Override
	public Integer create(RigaCarelloReq req) throws Exception {
		log.debug("create:" + req);
		Account ac = accountR.findById(req.getAccountID())
				.orElseThrow(() -> new Exception(validS.getMessaggio("account_ntfnd")));
				
		Carello car = new Carello();
		car.setAccount(ac);
		car.setDataCreazione(LocalDate.now());
		car.setStato(StatoCarello.valueOf("carello"));
		
		return carR.save(car).getId();
	}
	
	@Transactional (rollbackFor = Exception.class)	
	@Override
	public Integer addRiga(RigaCarelloReq req) throws Exception {
		log.debug("addRiga:" + req);
		Account acc = accountR.findById(req.getAccountID())
				.orElseThrow(() -> new Exception(validS.getMessaggio("account_ntfnd")));
		
		Carello carello =  null;
		int size = 0;
		if (acc.getCarello() == null) {
			int idCar = create(req);
			carello = carR.findById(idCar)
					.orElseThrow(() -> new Exception(validS.getMessaggio("carello_ntfnd")));
		} else {
			carello = acc.getCarello();
			size = acc.getCarello().getRigaCarello().size();
		}
			
		Optional.ofNullable(carello.getStato())
			.filter(stato -> stato == StatoCarello.valueOf("ordine"))
			.ifPresent(stato -> {
				throw new EcommerceException(validS.getMessaggio("carello_not_available"));
			});
		
		
		Prodotto prodotto = prodR.findById(req.getIdProdotto())
				.orElseThrow(() -> new Exception(validS.getMessaggio("prod_ntfnd")));
		
		Optional.ofNullable(req.getQuantita())
			.filter(q -> q > 0)
			.orElseThrow(() -> new Exception(validS.getMessaggio("carello_quantita_ko")));
				
		Supporto sup = buildSupporto(req.getSupporto());
		
		Prezzo prezzo = validS.searchSupporto(prodotto.getPrezzo(), sup);
				
		RigaCarello riga = new RigaCarello();
		riga.setDataCreazione(LocalDate.now());
		riga.setQuantita(req.getQuantita());
		riga.setCarello(carello);
		riga.setProdotto(prodotto);
		riga.setSupporto(sup);
		log.debug("Riga added...");
		
		if (prezzo.getStock() != null ) {
			stockS.pickItem(PickItemReq.builder()
					.prezzoId(prezzo.getId())
					.numeroItems(req.getQuantita())
					.build()
					);		
			log.debug("Stock updated...");			
		}
		rigaR.save(riga);
		
		return ++size;
	}
	
	
	@Transactional (rollbackFor = Exception.class)	
	@Override
	public void removeRiga(Integer id) throws Exception {
		log.debug("removeRiga:" + id);
		RigaCarello riga =  rigaR.findById(id)
				.orElseThrow(() -> new Exception(validS.getMessaggio("carello_elem_ko")));

		Optional.ofNullable(riga.getCarello().getStato())
			.filter(stato -> stato == StatoCarello.valueOf("ordine"))
			.ifPresent(stato -> {
				throw new EcommerceException(validS.getMessaggio("carello_not_available"));
			});


		
		Prezzo prezzo = validS.searchSupporto(riga.getProdotto().getPrezzo(), riga.getSupporto());

		if (prezzo.getStock() != null) {
			stockS.restoreItem(PickItemReq.builder()
					.prezzoId(prezzo.getId())
					.numeroItems(riga.getQuantita())
					.build()
					);
			
			log.debug("Stock updated...");
			
		}
		
		rigaR.delete(riga);
		
	}
	
	/*
	 * Questo metodo transforma la string supporto in enum
	 */
	private Supporto buildSupporto(String value) throws Exception{
		validS.checkNotNull(value, "prezzo_no_supporto");		
		try {
			return Supporto.valueOf(value);				
		} catch (IllegalArgumentException e) {
			throw new Exception(validS.getMessaggio("prezzo_no_supporto"));
		}
		
	}

}
