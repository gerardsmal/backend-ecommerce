package com.betacom.ecommerce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.ecommerce.dto.input.PrezzoReq;
import com.betacom.ecommerce.response.Response;
import com.betacom.ecommerce.services.interfaces.IPrezzoServices;
import com.betacom.ecommerce.services.interfaces.IStockServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

@RestController
@RequestMapping("rest/prezzo")
public class PrezzoController {

	private IPrezzoServices prezzoS;
	private IValidationServices validS;
	private IStockServices  stockS;

	public PrezzoController(IPrezzoServices prezzoS, IValidationServices validS, IStockServices  stockS) {
		this.prezzoS = prezzoS;
		this.validS = validS;
		this.stockS = stockS;
	}
	
	@PostMapping("/addPrezzo")
	public ResponseEntity<Response<String, Boolean>> create(@RequestBody (required = true) PrezzoReq req) {
		Response<String, Boolean> r = new Response<String, Boolean>();
		HttpStatus status = HttpStatus.OK;
		try {
			prezzoS.addPrezzo(req);
			r.setMsg(validS.getMessaggio("added"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	
	@PostMapping("/addPrezzoStock")
	public ResponseEntity<Response<String, Boolean>> createPrezzoStocke(@RequestBody (required = true) PrezzoReq req) {
		Response<String, Boolean> r = new Response<String, Boolean>();
		HttpStatus status = HttpStatus.OK;
		try {
			prezzoS.addPrezzoStock(req);
			r.setMsg(validS.getMessaggio("added"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Response<String, Boolean>> delete(@PathVariable (required = true) Integer id) {
		Response<String, Boolean> r = new Response<String, Boolean>();
		HttpStatus status = HttpStatus.OK;
		try {
			prezzoS.removePrezzo(id);
			r.setMsg(validS.getMessaggio("deleted"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	
}
