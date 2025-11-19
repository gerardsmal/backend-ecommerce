package com.betacom.ecommerce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.ecommerce.dto.input.AccountReq;
import com.betacom.ecommerce.dto.input.SigninReq;
import com.betacom.ecommerce.response.Response;
import com.betacom.ecommerce.services.interfaces.IAccountServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

@RestController
@RequestMapping("rest/account")
public class AccountController {

	private IAccountServices accS;
	private IValidationServices validS;

	public AccountController(IAccountServices accS, IValidationServices validS) {
		this.accS = accS;
		this.validS = validS;
	}

	@PostMapping("/create")
	public ResponseEntity<Response> create(@RequestBody(required = true) AccountReq req) {
	    Response response = new Response();
	    HttpStatus status = HttpStatus.OK;

	    try {
	        accS.create(req);
	        response.setMsg(validS.getMessaggio("created"));
	    } catch (Exception e) {
	        response.setMsg(e.getMessage());
	        status = HttpStatus.BAD_REQUEST;
	    }

	    return ResponseEntity.status(status).body(response);
	}

	@PutMapping("/update")
	public ResponseEntity<Response> update(@RequestBody(required = true) AccountReq req) {
	    Response response = new Response();
	    HttpStatus status = HttpStatus.OK;

	    try {
	        accS.update(req);
	        response.setMsg(validS.getMessaggio("updated"));
	    } catch (Exception e) {
	        response.setMsg(e.getMessage());
	        status = HttpStatus.BAD_REQUEST;
	    }

	    return ResponseEntity.status(status).body(response);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Response> delete(@PathVariable (required = true) Integer id) {
	    HttpStatus status = HttpStatus.OK;
	    Response response= new Response();
	    try {
	    	accS.delete(id);
	    	response.setMsg(validS.getMessaggio("deleted"));
	    } catch (Exception e) {
	        status = HttpStatus.BAD_REQUEST;
		    response.setMsg(e.getMessage());	    	        
	    }
	    return ResponseEntity.status(status).body(response);	    	

	}
	
	@PostMapping("/login")
	ResponseEntity<Object> login (@RequestBody (required = true) SigninReq req){
		  HttpStatus status = HttpStatus.OK;
		  Object r = new Object();
		try {
			r = accS.login(req);
		} catch (Exception e) {
			r = e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
		
	}
	
	@GetMapping("/list")
	public ResponseEntity<Object> list(
			@RequestParam (required = false) Integer id,
			@RequestParam (required = false) String nome,
			@RequestParam (required = false) String cognome,
			@RequestParam (required = false) String commune,
			@RequestParam (required = false) String stat,
			@RequestParam (required = false) String role
			){	
		Object r = new Object();
		HttpStatus status = HttpStatus.OK;
		try {
			r = accS.list(id, nome, cognome, commune, stat, role);
		} catch (Exception e) {
			r = e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
}
