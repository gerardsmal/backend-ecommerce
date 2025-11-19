package com.betacom.ecommerce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.ecommerce.dto.input.OrderReq;
import com.betacom.ecommerce.response.Response;
import com.betacom.ecommerce.services.interfaces.IOrderServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

@RestController
@RequestMapping("rest/order")
public class OrderController {

	private IOrderServices orderS;
	private IValidationServices validS;

	public OrderController(IOrderServices orderS, IValidationServices validS) {
		this.orderS = orderS;
		this.validS = validS;
	}

	
	@PostMapping("/create")
	public ResponseEntity<Response> create(@RequestBody (required = true) OrderReq req) {
		Response r = new Response(); 
		HttpStatus status = HttpStatus.OK;
		try {
			orderS.create(req);
			r.setMsg(validS.getMessaggio("created"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}

	@PostMapping("/confirm")
	public ResponseEntity<Response> confirm(@RequestBody (required = true) OrderReq req) {
		Response r = new Response(); 
		HttpStatus status = HttpStatus.OK;
		try {
			orderS.confirm(req);
			r.setMsg(validS.getMessaggio("confirmed"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}

	
	
	@DeleteMapping("/delete")
	public ResponseEntity<Response> deleteRiga(@RequestBody (required = true) OrderReq req) {
		Response r = new Response();
		HttpStatus status = HttpStatus.OK;
		try {
			orderS.remove(req);
			r.setMsg(validS.getMessaggio("deleted"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	
	@GetMapping("/list")
	public ResponseEntity<Object> list(@RequestParam (required = true) Integer id){	
		Object r = new Object();
		HttpStatus status = HttpStatus.OK;
		try {
			r=orderS.listByAccountId(id);
		} catch (Exception e) {
			r=e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
}
