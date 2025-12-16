package com.betacom.ecommerce.services.interfaces;

import com.betacom.ecommerce.dto.output.OrderDTO;

public interface IExcelServices {

	byte[] exportOrder(OrderDTO order) throws Exception;
}
