package com.betacom.ecommerce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.ecommerce.response.Response;
import com.betacom.ecommerce.services.interfaces.IModalitaPagamentoServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("rest/pagamento")
public class ModalitaPagamentoController {

	private final IModalitaPagamentoServices pagaS;

	public ModalitaPagamentoController(IModalitaPagamentoServices pagaS) {
		this.pagaS = pagaS;
	}
	
	
	
	@GetMapping("/list")
	public ResponseEntity<Object> list(){	
		Object r = new Object();
		HttpStatus status = HttpStatus.OK;
		try {
			r = pagaS.list();
		} catch (Exception e) {
			r = e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	
	@GetMapping("/getModalita")
	public ResponseEntity<Object> getAccount( @RequestParam (required = false) Integer id){	
		
		HttpStatus status = HttpStatus.OK;
		try {		
			return ResponseEntity.status(status).body(pagaS.getModalita(id));
		} catch (Exception e) {
			Response<String, Boolean> r = new Response<String, Boolean>();
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
			return ResponseEntity.status(status).body(r);
		}
	}
}
