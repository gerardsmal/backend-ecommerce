package com.betacom.ecommerce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.ecommerce.dto.input.RigaCarelloReq;
import com.betacom.ecommerce.response.Response;
import com.betacom.ecommerce.services.interfaces.ICarelloServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("rest/carello")
public class CarelloController {
	private final ICarelloServices carS;
	private final IValidationServices validS;
	

	
	@PostMapping("/addRiga")
	public ResponseEntity<Object> addRiga(@RequestBody (required = true) RigaCarelloReq req) {
		
		HttpStatus status = HttpStatus.OK;
		try {
			Response<String, Integer>  r = new Response<String, Integer>();
			r.setResult(carS.addRiga(req));
			r.setMsg(validS.getMessaggio("carello_added"));
			return ResponseEntity.status(status).body(r);	
		} catch (Exception e) {
			Response<String, Boolean> r = new Response<String, Boolean>();
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
			return ResponseEntity.status(status).body(r);		
		}		
	}

	
	@PutMapping("/updateRiga")
	public ResponseEntity<Object> updateRiga(@RequestBody (required = true) RigaCarelloReq req) {
		Response<String, Boolean> r = new Response<String, Boolean>();
		HttpStatus status = HttpStatus.OK;
		try {
			carS.updateQta(req);
			r.setMsg(validS.getMessaggio("updated"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}		
		return ResponseEntity.status(status).body(r);	

	}

	
	@DeleteMapping("/deleteRiga/{id}")
	public ResponseEntity<Response<String, Boolean>> deleteRiga(@PathVariable (required = true) Integer id) {
		Response<String, Boolean> r = new Response<String, Boolean>();
		HttpStatus status = HttpStatus.OK;
		try {
			carS.removeRiga(id);
			r.setMsg(validS.getMessaggio("deleted"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}

}
