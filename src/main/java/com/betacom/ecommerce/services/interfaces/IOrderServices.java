package com.betacom.ecommerce.services.interfaces;

import java.util.List;

import com.betacom.ecommerce.dto.input.OrderReq;
import com.betacom.ecommerce.dto.output.OrderDTO;

public interface IOrderServices {

	void create(OrderReq req) throws Exception;
	void remove(Integer id) throws Exception;
	OrderDTO confirm(OrderReq req) throws Exception;
	
	Boolean getOrderStatus(Integer id) throws Exception;

	List<OrderDTO> listByAccountId(Integer id, String productName, String artist, String genere) throws Exception;
	OrderDTO getLastOrdine(Integer id) throws Exception;
}
