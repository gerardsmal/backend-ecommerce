package com.betacom.ecommerce.controllers;

import org.springframework.data.repository.query.Param;
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

import com.betacom.ecommerce.dto.input.FamigliaReq;
import com.betacom.ecommerce.response.Response;
import com.betacom.ecommerce.services.interfaces.IFamigliaServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

@RestController
@RequestMapping("rest/famiglia")
public class FamigliaController {

	private IFamigliaServices famS;
	private IValidationServices validS;
	
	public FamigliaController(IFamigliaServices famS, IValidationServices validS) {
		this.famS = famS;
		this.validS = validS;
	}

	
	@PostMapping("/create")
	public ResponseEntity<Response> create(@RequestBody (required = true) FamigliaReq req) {
		Response r = new Response(); 
		HttpStatus status = HttpStatus.OK;
		try {
			famS.create(req);
			r.setMsg(validS.getMessaggio("created"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}

	@PutMapping("/update")
	public ResponseEntity<Response> update(@RequestBody (required = true) FamigliaReq req) {
		Response r = new Response();
		HttpStatus status = HttpStatus.OK;
		try {
			famS.update(req);
			r.setMsg(validS.getMessaggio("updated"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}


	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Response> delete(@PathVariable (required = true) Integer id) {

		Response r = new Response();
		HttpStatus status = HttpStatus.OK;
		try {
			famS.delete(id);
			r.setMsg(validS.getMessaggio("deleted"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	
	@GetMapping("/list")
	public ResponseEntity<Object> list(@RequestParam (required = false) String pattern){
		Object r = new Object();
		HttpStatus status = HttpStatus.OK;
		try {
			r=famS.list(pattern);
		} catch (Exception e) {
			r=e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	
	@GetMapping("/listByProduct")
	public ResponseEntity<Object> listByProduct(){
		Object r = new Object();
		HttpStatus status = HttpStatus.OK;
		try {
			r=famS.listPerFamiglia();
		} catch (Exception e) {
			r=e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}

	@GetMapping("/listProductbyId")
	public ResponseEntity<Object> listProductbyId(@RequestParam (required = true) Integer id){
		Object r = new Object();
		HttpStatus status = HttpStatus.OK;
		try {
			r=famS.ListByIdProdottoFamiglia(id);
		} catch (Exception e) {
			r=e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	
	
}
