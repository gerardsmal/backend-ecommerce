package com.betacom.ecommerce.services.interfaces;

import com.betacom.ecommerce.dto.input.PickItemReq;
import com.betacom.ecommerce.dto.input.StockReq;
import com.betacom.ecommerce.exception.EcommerceException;

public interface IStockServices {

	void update(StockReq req) throws EcommerceException;
	void delete(StockReq req) throws Exception;
	void pickItem(PickItemReq req) throws Exception;
	void restoreItem(PickItemReq req) throws Exception;

}
