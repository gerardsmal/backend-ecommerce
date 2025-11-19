package com.betacom.ecommerce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.ecommerce.dto.input.CarelloReq;
import com.betacom.ecommerce.dto.input.RigaCarelloReq;
import com.betacom.ecommerce.response.Response;
import com.betacom.ecommerce.services.interfaces.ICarelloServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

@RestController
@RequestMapping("rest/carello")
public class CarelloController {
	private ICarelloServices carS;
	private IValidationServices validS;
	
	public CarelloController(ICarelloServices carS, IValidationServices validS) {
		this.carS = carS;
		this.validS = validS;
	}

	@PostMapping("/create")
	public ResponseEntity<Response> create(@RequestBody (required = true) CarelloReq req) {
		Response r = new Response();
		HttpStatus status = HttpStatus.OK;
		try {
			carS.create(req);
			r.setMsg(validS.getMessaggio("created"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	
	@PostMapping("/addRiga")
	public ResponseEntity<Response> addRiga(@RequestBody (required = true) RigaCarelloReq req) {
		Response r = new Response();
		HttpStatus status = HttpStatus.OK;
		try {
			carS.addRiga(req);
			r.setMsg(validS.getMessaggio("created"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);	}
	
	@DeleteMapping("/deleteRiga")
	public ResponseEntity<Response> deleteRiga(@RequestBody (required = true) RigaCarelloReq req) {
		Response r = new Response();
		HttpStatus status = HttpStatus.OK;
		try {
			carS.removeRiga(req);
			r.setMsg(validS.getMessaggio("deleted"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}

}
