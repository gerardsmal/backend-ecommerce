package com.betacom.ecommerce.services.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.ecommerce.dto.input.OrderReq;
import com.betacom.ecommerce.dto.output.OrderDTO;
import com.betacom.ecommerce.dto.output.OrderItemDTO;
import com.betacom.ecommerce.dto.output.SpedizioneDTO;
import com.betacom.ecommerce.enums.StatoCarello;
import com.betacom.ecommerce.enums.StatusPagamento;
import com.betacom.ecommerce.models.Account;
import com.betacom.ecommerce.models.Carello;
import com.betacom.ecommerce.models.ModalitaPagamento;
import com.betacom.ecommerce.models.Order;
import com.betacom.ecommerce.models.OrderItems;
import com.betacom.ecommerce.models.Prezzo;
import com.betacom.ecommerce.models.RigaCarello;
import com.betacom.ecommerce.models.Spedizione;
import com.betacom.ecommerce.repositories.IAccountRepository;
import com.betacom.ecommerce.repositories.ICarelloRepository;
import com.betacom.ecommerce.repositories.IModalidaPagamentoRepository;
import com.betacom.ecommerce.repositories.IOrderItemsRepository;
import com.betacom.ecommerce.repositories.IOrderRepository;
import com.betacom.ecommerce.repositories.IRigaCarelloRepository;
import com.betacom.ecommerce.repositories.ISpedizioneRepository;
import com.betacom.ecommerce.services.interfaces.IOrderCounterServices;
import com.betacom.ecommerce.services.interfaces.IOrderServices;
import com.betacom.ecommerce.services.interfaces.IUploadServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderImpl implements IOrderServices{

	private final IAccountRepository accountR;
	private final IOrderRepository   orderR;
	private final IValidationServices validS;
	private final IOrderItemsRepository itemR;
	private final ICarelloRepository    carelloR;
	private final IRigaCarelloRepository rigaCarelloR;
	private final IOrderCounterServices  countS;
	private final ISpedizioneRepository  spedR;
	private final IModalidaPagamentoRepository  modR;
	private final IUploadServices  uploadS;
	
	@Override
	public Boolean getOrderStatus(Integer id) throws Exception {
		log.debug("getOrderStatus:" + id);
		Account ac = accountR.findById(id)
				.orElseThrow(() -> new Exception(validS.getMessaggio("account_ntfnd")));
		
		
		if (ac.getCarello().getStato() == StatoCarello.valueOf("ordine"))
			return false;		
		
		
		return true;
		
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
		
		Spedizione spedi = spedR.findById(req.getSpedizioneID())
				.orElseThrow(() -> new Exception(validS.getMessaggio("spedizione_ntfnd")));
		
		ModalitaPagamento modalita = modR.findById(req.getModalitaID())
				.orElseThrow(() -> new Exception(validS.getMessaggio("modalita_ntfnd")));
		
		
		order.setDataOrdine(LocalDate.now());
		
		order.setNumeroOrdine(countS.nextOrderProvisaryNumber());  // set numero provisorio 
		order.setSpedizione(spedi);
		order.setModalitaPagamento(modalita);
		
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
			item.setImage(riga.getProdotto().getImage());
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
	public void remove(Integer id) throws Exception {
		log.debug("Remove {}:" ,id);
		Account ac = accountR.findById(id)
				.orElseThrow(() -> new Exception(validS.getMessaggio("account_ntfnd")));
		
		Order order = retrieveCurrentOrder(ac);
		
		// control order status
		Optional.ofNullable(order.getStatusPagamento())
			.filter(status -> status.equals(StatusPagamento.valueOf("IN_CORSO")))
			.orElseThrow(() -> new Exception(validS.getMessaggio("order_not_cancelabile")));
		
		Carello carello = order.getAccount().getCarello();
		
		orderR.delete(order);
		
		updateCarelloStatus(carello, "carello");
		
	}
	
	@Transactional (rollbackFor = Exception.class)	
	private Order retrieveCurrentOrder(Account account) throws Exception{
		List<Order> lO = orderR.findByAccountAndStatusPagamento(account, StatusPagamento.valueOf("IN_CORSO"));
		if (lO.size() > 1) throw new Exception("numero di ordine in corso > 1");
		return lO.getFirst();
		
	}
	
	@Transactional (rollbackFor = Exception.class)	
	@Override
	public OrderDTO confirm(OrderReq req) throws Exception {
		log.debug("confirm:" + req);
		
		Account ac = accountR.findById(req.getAccountID())
				.orElseThrow(() -> new Exception(validS.getMessaggio("account_ntfnd")));
		
		Order order = retrieveCurrentOrder(ac);
		
		try {
			order.setStatusPagamento(StatusPagamento.valueOf("PAGATO"));	
		} catch (IllegalArgumentException e) {
			throw new Exception(validS.getMessaggio("order_status_invalid"));
		}
		
		order.setNumeroOrdine(countS.nextOrderNumber());
		order.setDataInvio(LocalDate.now());
		orderR.save(order);
		
		rigaCarelloR.removeItems(order.getAccount().getCarello().getId());
		log.debug("After remove riga carello");
		
		updateCarelloStatus(order.getAccount().getCarello(), "carello");
		log.debug("After update status carello");
		
		return getLastOrdine(req.getAccountID());
	}


	@Override
	public List<OrderDTO> listByAccountId(Integer id,  String producName, String artist, String genere) throws Exception {
		log.debug("listByAccountId: {} / {} / {}", id, producName, artist);
		
		String prod = (producName == null) ? null : producName.toUpperCase();
		String art = (artist == null) ? null : artist.toUpperCase();
		String gen = (genere == null) ? null : genere.toUpperCase();
		
		
		List<Order> lO = orderR.searchByFilter(id, prod, art, gen);
		
		return lO.stream()
				.map(o -> OrderDTO.builder()
						.numeroOrdine(o.getNumeroOrdine())
						.dataOrdine(o.getDataOrdine())
						.dataInvio(o.getDataInvio())
						.id(o.getId())
						.modalitaPagamento(o.getModalitaPagamento().getTipo())
						.prezzoTotale(o.getTotale())
						.status(o.getStatusPagamento().toString())
						.spedizione(buildSprdizione(o.getSpedizione()))
						.modalitaPagamento(o.getModalitaPagamento().getTipo())
						.riga(buildRigaOrdine(o.getOrderItems()))
						.build())
				.toList();
	}
	
	@Override
	public OrderDTO getLastOrdine(Integer id) throws Exception {
		log.debug("getLastOrdine: {} ", id);
		
		Account ac = accountR.findById(id)
				.orElseThrow(() -> new Exception(validS.getMessaggio("account_ntfnd")));
		
		Order o = ac.getOrders().getFirst();
		
		return OrderDTO.builder()
						.numeroOrdine(o.getNumeroOrdine())
						.dataOrdine(o.getDataOrdine())
						.dataInvio(o.getDataInvio())
						.id(o.getId())
						.modalitaPagamento(o.getModalitaPagamento().getTipo())
						.prezzoTotale(o.getTotale())
						.status(o.getStatusPagamento().toString())
						.spedizione(buildSprdizione(o.getSpedizione()))
						.modalitaPagamento(o.getModalitaPagamento().getTipo())
						.riga(buildRigaOrdine(o.getOrderItems()))
						.build();
	}
	


	private SpedizioneDTO buildSprdizione(Spedizione s) {
		return SpedizioneDTO.builder()
				.id(s.getId())
				.isDefault(s.getPredefinito())
				.nome(s.getNome())
				.cognome(s.getCognome())
				.via(s.getVia())
				.cognome(s.getCommune())
				.cap(s.getCap())
				.build();
	}
	
	private List<OrderItemDTO> buildRigaOrdine(List<OrderItems> riga){
		return riga.stream()
				.map(r -> OrderItemDTO.builder()
						.artist(r.getArtist())
						.dataCreazione(r.getDataCreazione())
						.productName(r.getProductName())
						.genere(r.getGenere())
						.id(r.getId())
						.prezzoDaPagare(r.getPrezzo())
						.prezzoUnitario(r.getPrezzoUnit())
						.quantita(r.getQuantita())
						.supporto(r.getSupporto().toString())
						.image(r.getImage() == null ? null : uploadS.buildUrl(r.getImage()))
						.build())
				.toList();
	
	}
	
	private void updateCarelloStatus(Carello carello, String status) {
		carello.setStato(StatoCarello.valueOf(status));
		carelloR.save(carello);
	}



}
