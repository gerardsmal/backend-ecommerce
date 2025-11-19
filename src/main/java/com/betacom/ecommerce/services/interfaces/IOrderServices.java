package com.betacom.ecommerce.services.interfaces;

import java.util.List;

import com.betacom.ecommerce.dto.input.OrderReq;
import com.betacom.ecommerce.dto.output.OrderDTO;

public interface IOrderServices {

	void create(OrderReq req) throws Exception;
	void remove(OrderReq req) throws Exception;
	void confirm(OrderReq req) throws Exception;

	List<OrderDTO> listByAccountId(Integer id) throws Exception;
}
