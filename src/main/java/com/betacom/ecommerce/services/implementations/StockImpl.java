package com.betacom.ecommerce.services.implementations;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.ecommerce.dto.input.PickItemReq;
import com.betacom.ecommerce.dto.input.StockReq;
import com.betacom.ecommerce.exception.EcommerceException;
import com.betacom.ecommerce.models.Prezzo;
import com.betacom.ecommerce.models.Stock;
import com.betacom.ecommerce.repositories.IPrezzoRepository;
import com.betacom.ecommerce.repositories.IStockRepositoy;
import com.betacom.ecommerce.services.interfaces.IValidationServices;
import com.betacom.ecommerce.services.interfaces.IStockServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class StockImpl implements IStockServices{
	private final IStockRepositoy stockR;
	private final IPrezzoRepository prezzoR;
	private final IValidationServices msgS;
	
	
	@Transactional (rollbackFor = Exception.class)
	@Override
	public void update(StockReq req) throws EcommerceException {
		log.debug("update :" + req);
		Prezzo prez = prezzoR.findById(req.getPrezzoId())
				.orElseThrow(() -> new EcommerceException(msgS.getMessaggio("prezzo_ntfnd")));
		
		// load instance if exist else create new instance
		Stock stock = Objects.requireNonNullElseGet(prez.getStock(), Stock::new);
		
		Optional.ofNullable(req.getCurrentStock()).ifPresent(stock::setCurrentStock);
		Optional.ofNullable(req.getStockAlert()).ifPresent(stock::setStockAlert);
		
				
		/*
		 * control stock
		 */
		Optional.ofNullable(stock.getCurrentStock())
				.orElseThrow(() -> new EcommerceException(msgS.getMessaggio("stock_no_current")));
		Optional.ofNullable(stock.getStockAlert())
				.orElseThrow(() -> new EcommerceException(msgS.getMessaggio("stock_no_alert")));

		
		stockR.save(stock);
		log.debug("Stocke saved");
		
		prez.setStock(stock);	
		
		prezzoR.save(prez);		
	}

	@Transactional (rollbackFor = Exception.class)
	@Override
	public void delete(StockReq req) throws Exception {
		log.debug("delete :" + req);
		Prezzo prez = prezzoR.findById(req.getPrezzoId())
				.orElseThrow(() -> new Exception(msgS.getMessaggio("prezzo_ntfnd")));

		Optional.ofNullable(prez.getStock())
			.orElseThrow(() -> new Exception(msgS.getMessaggio("stock_ntfnd")));
				
		int id = prez.getStock().getId();
		
		prez.setStock(null);
		prezzoR.save(prez);
		log.debug("save prezzo");
		
		
		stockR.deleteById(id);

	}
	@Transactional (rollbackFor = Exception.class)
	@Override
	public void pickItem(PickItemReq req) throws Exception {
		log.debug("pickItem :" + req);

		Prezzo prez = prezzoR.findById(req.getPrezzoId())
				.orElseThrow(() -> new Exception(msgS.getMessaggio("prezzo_ntfnd")));

		msgS.checkNotNull(prez.getStock(), "stock_ntfnd");

		Stock st = prez.getStock();
		
		st.setCurrentStock(st.getCurrentStock() - req.getNumeroItems());
		
		Optional.ofNullable(st.getCurrentStock())       // stock must be positive
			.filter(q -> q > 0)
			.orElseThrow(() -> new Exception(msgS.getMessaggio("stock_no_qta")));
		
		stockR.save(st);
		
	}

	@Override
	public void restoreItem(PickItemReq req) throws Exception {
		log.debug("restoreItem:" + req);
		Prezzo prez = prezzoR.findById(req.getPrezzoId())
				.orElseThrow(() -> new Exception(msgS.getMessaggio("prezzo_ntfnd")));
		
		msgS.checkNotNull(prez.getStock(), "stock_ntfnd");
		Stock st = prez.getStock();
		
		st.setCurrentStock(st.getCurrentStock() + req.getNumeroItems());
		stockR.save(st);
	}

}
