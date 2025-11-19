package com.betacom.ecommerce.services.implementations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.ecommerce.dto.input.PrezzoReq;
import com.betacom.ecommerce.dto.input.StockReq;
import com.betacom.ecommerce.enums.Supporto;
import com.betacom.ecommerce.enums.SupportoDigitale;
import com.betacom.ecommerce.exception.EcommerceException;
import com.betacom.ecommerce.models.Prezzo;
import com.betacom.ecommerce.models.Prodotto;
import com.betacom.ecommerce.repositories.IPrezzoRepository;
import com.betacom.ecommerce.repositories.IProdottoRepository;
import com.betacom.ecommerce.services.interfaces.IValidationServices;
import com.betacom.ecommerce.services.interfaces.IPrezzoServices;
import com.betacom.ecommerce.services.interfaces.IStockServices;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class PrezzoImpl implements IPrezzoServices{

	private IPrezzoRepository prezzoR;
	private IProdottoRepository prodR;
	private IValidationServices   msgS;
	private IStockServices      stockS;
	
	
	public PrezzoImpl(IPrezzoRepository prezzoR, 
			IProdottoRepository prodR, 
			IValidationServices   msgS, 
			IStockServices      stockS) {
		this.prezzoR = prezzoR;
		this.prodR = prodR;
		this.msgS  = msgS;
		this.stockS = stockS;
	}

	@Transactional (rollbackFor = Exception.class)
	@Override
	public Integer addPrezzo(PrezzoReq req) throws Exception {
		log.debug("addPrezzo:" + req);

		Prodotto prod = prodR.findById(req.getIdProdotto())
				.orElseThrow(() -> new Exception(msgS.getMessaggio("prod_ntfnd")));		
		
		log.debug("after prod..");
		
		Prezzo pr;	
		Supporto sup = null;
			
		if (req.getSupporto() != null) {
			sup = Optional.ofNullable(Supporto.safeValueOf(req.getSupporto()))		
					.orElseThrow(() -> new Exception(msgS.getMessaggio("prezzo_ntfnd")));
		}
		
		if (req.getId() == null) {
			log.debug("after support null..");
		    msgS.checkNotNull(sup, "prezzo_no_supporto");
		    
		    if (searchSupporto(prod.getPrezzo(), sup)) throw new Exception(msgS.getMessaggio("prezzo_exist"));
		    		    
		    pr = new Prezzo();
			pr.setProdotto(prod);
		} else {
			log.debug("after support.." + req.getId());
		    pr = prezzoR.findById(req.getId())
		        .orElseThrow(() -> new Exception(msgS.getMessaggio("prezzo_ntfnd")));
		}
		
		log.debug("after prezzo:");
		if (sup != null) {
		    pr.setSupporto(sup);
		}
		
		
		if (req.getPrezzo() != null) {
			pr.setPrezzo(req.getPrezzo());
		}
		
		
		return prezzoR.save(pr).getId();
	}
	private Boolean searchSupporto(List<Prezzo> prezzi, Supporto sup) {
	    return prezzi.stream()
	                 .anyMatch(p -> p.getSupporto() == sup);
	}

	
	@Transactional (rollbackFor = Exception.class)
	@Override
	public void addPrezzoStock(PrezzoReq req) throws Exception{
		log.debug("addPrezzoStock:" + req);
		Integer prezzoID = addPrezzo(req);
		log.debug("after addPrezzo id:" + prezzoID);
		SupportoDigitale sup = null;
		boolean isDigitale = false;
		
		Prezzo pr = prezzoR.findById(prezzoID)
		        .orElseThrow(() -> new Exception(msgS.getMessaggio("prezzo_ntfnd")));
		
		sup = SupportoDigitale.safeValueOf(pr.getSupporto().toString());  // control digital product
		isDigitale = (sup != null);

		log.debug("after digital :" + isDigitale);
		if (isDigitale) {
		    Optional.ofNullable(req.getCurrentStock())
		        .ifPresent(s -> {
		            throw new EcommerceException(msgS.getMessaggio("stock_not_valid"));
		        });
		} else {
		    Integer stock = Optional.ofNullable(req.getCurrentStock())
		        .orElseThrow(() -> new EcommerceException(msgS.getMessaggio("stock_mandatory")));
		    log.debug("After stock");
		    
		    stockS.update(new StockReq(prezzoID, stock, req.getStockAlert()));
		}

		
	}

	
	
	@Transactional (rollbackFor = Exception.class)
	@Override
	public void removePrezzo(Integer id) throws Exception {
		log.debug("addPrezzo:" + id);
		
		Prezzo pr = prezzoR.findById(id)
				.orElseThrow(() -> new Exception(msgS.getMessaggio("prezzo_ntfnd")));
		
		prezzoR.delete(pr);
		
	}
	
	
	

}
