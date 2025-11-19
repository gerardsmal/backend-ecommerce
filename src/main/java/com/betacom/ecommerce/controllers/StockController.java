package com.betacom.ecommerce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.ecommerce.dto.input.StockReq;
import com.betacom.ecommerce.response.Response;
import com.betacom.ecommerce.services.interfaces.IStockServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

@RestController
@RequestMapping("rest/stock")
public class StockController {
	private IStockServices stockS;
	private IValidationServices validS;
	
	public StockController(IStockServices stockS, IValidationServices validS) {
		this.stockS = stockS;
		this.validS = validS;
	}

	@PostMapping("/update")
	public ResponseEntity<Response> update(@RequestBody (required = true) StockReq req) {
		Response r = new Response();
		HttpStatus status = HttpStatus.OK;
		try {
			stockS.update(req);
			r.setMsg(validS.getMessaggio("updated"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<Response> delete(@RequestBody (required = true) StockReq req) {
		Response r = new Response();
		HttpStatus status = HttpStatus.OK;
		try {
			stockS.delete(req);
			r.setMsg(validS.getMessaggio("deleted"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}

	
}
