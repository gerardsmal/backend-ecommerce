package com.betacom.ecommerce.services.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.ecommerce.dto.input.OrderReq;
import com.betacom.ecommerce.dto.output.OrderDTO;
import com.betacom.ecommerce.dto.output.OrderItemDTO;
import com.betacom.ecommerce.enums.StatoCarello;
import com.betacom.ecommerce.enums.StatusPagamento;
import com.betacom.ecommerce.models.Account;
import com.betacom.ecommerce.models.Carello;
import com.betacom.ecommerce.models.Order;
import com.betacom.ecommerce.models.OrderItems;
import com.betacom.ecommerce.models.Prezzo;
import com.betacom.ecommerce.models.RigaCarello;
import com.betacom.ecommerce.repositories.IAccountRepository;
import com.betacom.ecommerce.repositories.ICarelloRepository;
import com.betacom.ecommerce.repositories.IOrderItemsRepository;
import com.betacom.ecommerce.repositories.IOrderRepository;
import com.betacom.ecommerce.repositories.IRigaCarelloRepository;
import com.betacom.ecommerce.services.interfaces.IOrderServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderImpl implements IOrderServices{

	private IAccountRepository accountR;
	private IOrderRepository   orderR;
	private IValidationServices validS;
	private IOrderItemsRepository itemR;
	private ICarelloRepository    carelloR;
	private IRigaCarelloRepository rigaCarelloR;
	
	public OrderImpl(IAccountRepository accountR, 
			IOrderRepository orderR, 
			IValidationServices validS, 
			IOrderItemsRepository itemR,
			ICarelloRepository    carelloR,
			IRigaCarelloRepository rigaCarelloR) {
		this.accountR = accountR;
		this.orderR = orderR;
		this.validS = validS;
		this.itemR = itemR;
		this.carelloR = carelloR;
		this.rigaCarelloR = rigaCarelloR;
	}
	
	@Transactional (rollbackFor = Exception.class)	
	@Override
	public void create(OrderReq req) throws Exception {
		log.debug("create:" + req);
		Account ac = accountR.findById(req.getAccountID())
				.orElseThrow(() -> new Exception(validS.getMessaggio("account_ntfnd")));

		if (ac.getCarello().getStato() == StatoCarello.valueOf("ordine"))
			throw new Exception(validS.getMessaggio("order_not_available"));

		if (ac.getCarello().getRigaCarello().isEmpty())
			throw new Exception(validS.getMessaggio("order_carello_empty"));
		
		Order order = new Order();
		try {
			order.setStatusPagamento(StatusPagamento.valueOf("IN_CORSO"));	
		} catch (IllegalArgumentException e) {
			throw new Exception(validS.getMessaggio("order_status_invalid"));
		}
		
		order.setDataOrdine(LocalDate.now());
		order.setAccount(ac);
		
		Integer id =orderR.save(order).getId();		
		buildOrderItems(id);
		
	}
	
	@Transactional (rollbackFor = Exception.class)	
	private void buildOrderItems(Integer orderID) throws Exception{
		log.debug("buildOrderItems id:" + orderID);
		Order order = orderR.findById(orderID)
				.orElseThrow(() -> new Exception(validS.getMessaggio("order_ntfnd")));
		
		double totale = 0;
		for (RigaCarello riga: order.getAccount().getCarello().getRigaCarello()) {
			OrderItems item = new OrderItems();
			item.setDataCreazione(riga.getDataCreazione());
			item.setQuantita(riga.getQuantita());
			item.setProductName(riga.getProdotto().getDescrizione());
			item.setArtist(riga.getProdotto().getArtista().getNome());
			item.setGenere(riga.getProdotto().getFamiglia().getDescrizione());
			item.setSupporto(riga.getSupporto());
			Prezzo prezzo = validS.searchSupporto(riga.getProdotto().getPrezzo(), riga.getSupporto());
			item.setPrezzoUnit(prezzo.getPrezzo());
			item.setPrezzo(item.getPrezzoUnit() * riga.getQuantita());
			item.setOrder(order);
			totale = totale + item.getPrezzo();
			itemR.save(item);	
			log.debug("totale:" + totale);
						
		}
		
		order.setTotale(totale);   // update order with total
		orderR.save(order);
		
		updateCarelloStatus(order.getAccount().getCarello(), "ordine");   // lock carello
	}
	
	@Transactional (rollbackFor = Exception.class)	
	@Override
	public void remove(OrderReq req) throws Exception {
		log.debug("Remove :" + req);
		Order order = orderR.findById(req.getId())
				.orElseThrow(() -> new Exception(validS.getMessaggio("order_ntfnd")));
		
		// control order status
		Optional.ofNullable(order.getStatusPagamento())
			.filter(status -> status.equals(StatusPagamento.valueOf("IN_CORSO")))
			.orElseThrow(() -> new Exception(validS.getMessaggio("order_not_cancelabile")));
		
		Carello carello = order.getAccount().getCarello();
		
		orderR.delete(order);
		
		updateCarelloStatus(carello, "carello");
		
	}
	
	@Transactional (rollbackFor = Exception.class)	
	@Override
	public void confirm(OrderReq req) throws Exception {
		log.debug("confirm:" + req);
		Order order = orderR.findById(req.getId())
				.orElseThrow(() -> new Exception(validS.getMessaggio("order_ntfnd")));
		try {
			order.setStatusPagamento(StatusPagamento.valueOf("PAGATO"));	
		} catch (IllegalArgumentException e) {
			throw new Exception(validS.getMessaggio("order_status_invalid"));
		}
		
		order.setDataInvio(LocalDate.now());
		orderR.save(order);
		
		rigaCarelloR.removeItems(order.getAccount().getCarello().getId());
		log.debug("After remove riga carello");
		
		updateCarelloStatus(order.getAccount().getCarello(), "carello");
		log.debug("After update status carello");
	}


	@Override
	public List<OrderDTO> listByAccountId(Integer id) throws Exception {
		log.debug("listByAccountId:" + id);
		Account ac = accountR.findById(id)
				.orElseThrow(() -> new Exception(validS.getMessaggio("account_ntfnd")));
		
		return ac.getOders().stream()
				.map(o -> OrderDTO.builder()						
						.dataOrdine(o.getDataOrdine())
						.dataInvio(o.getDataInvio())
						.id(o.getId())
						.prezzoTotale(o.getTotale())
						.status(o.getStatusPagamento().toString())
						.riga(buildRigaOrdine(o.getOrderItems()))
						.build())
				.toList();
	}

	private List<OrderItemDTO> buildRigaOrdine(List<OrderItems> riga){
		return riga.stream()
				.map(r -> OrderItemDTO.builder()
						.artist(r.getArtist())
						.dataCreazione(r.getDataCreazione())
						.productName(r.getProductName())
						.genere(r.getGenere())
						.id(r.getId())
						.prezzoDaPagare(r.getPrezzoUnit())
						.prezzoUnitario(r.getPrezzoUnit())
						.quantita(r.getQuantita())
						.supporto(r.getSupporto().toString())
						.build())
				.toList();
	
	}
	
	private void updateCarelloStatus(Carello carello, String status) {
		carello.setStato(StatoCarello.valueOf(status));
		carelloR.save(carello);
	}


}
