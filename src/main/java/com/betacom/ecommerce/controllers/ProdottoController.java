package com.betacom.ecommerce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.ecommerce.dto.input.ProdottoReq;
import com.betacom.ecommerce.response.Response;
import com.betacom.ecommerce.services.interfaces.IProdottoServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("rest/prodotto")
public class ProdottoController {

	private final IProdottoServices prodS;
	private final IValidationServices validS;
	
	
	@PostMapping("/create")
	public ResponseEntity<Response<String, Integer>> create(@RequestBody (required = true) ProdottoReq req) {
		Response<String, Integer> r = new Response<String, Integer>();
		HttpStatus status = HttpStatus.OK;
		try {
			r.setResult(prodS.create(req));
			r.setMsg(validS.getMessaggio("created"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status= HttpStatus.BAD_REQUEST;
		}
		
		 return ResponseEntity.status(status).body(r);
	}
	
	
	@PutMapping("/update")
	public ResponseEntity<Response<String, Boolean>> update(@RequestBody (required = true) ProdottoReq req) {
		Response<String, Boolean> r = new Response<String, Boolean>();
		HttpStatus status = HttpStatus.OK;
		try {
			prodS.update(req);
			r.setMsg(validS.getMessaggio("updated"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status= HttpStatus.BAD_REQUEST;
		}
		
		 return ResponseEntity.status(status).body(r);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Response<String, Boolean>> delete(@PathVariable (required = true) Integer id) {
		Response<String, Boolean> r = new Response<String, Boolean>();
		HttpStatus status = HttpStatus.OK;
		try {
			prodS.delete(id);
			r.setMsg(validS.getMessaggio("deleted"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status= HttpStatus.BAD_REQUEST;
		}
		
		return ResponseEntity.status(status).body(r);
	}
	
	
	@GetMapping("/list")
	public  ResponseEntity<Object> list(
			@RequestParam (required = false) Integer id,
			@RequestParam (required = false) String desc,
			@RequestParam (required = false) Integer artist,
			@RequestParam (required = false) Integer famiglia
			){
		
		Object r = new Object();
		HttpStatus status = HttpStatus.OK;
		try {
			r= prodS.list(id,desc, artist, famiglia);
		} catch (Exception e) {
			r= e.getMessage();
			status= HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	
	@GetMapping("/getById")
	public  ResponseEntity<Object> getById(@RequestParam (required = true) Integer id){
		
		Object r = new Object();
		HttpStatus status = HttpStatus.OK;
		try {
			r= prodS.getById(id);
		} catch (Exception e) {
			r= e.getMessage();
			status= HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	
}
