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

import com.betacom.ecommerce.dto.input.OrderReq;
import com.betacom.ecommerce.dto.input.SpedizioneReq;
import com.betacom.ecommerce.response.Response;
import com.betacom.ecommerce.services.interfaces.IOrderServices;
import com.betacom.ecommerce.services.interfaces.ISpedizioneServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

@RestController
@RequestMapping("rest/order")
public class OrderController {

	private IOrderServices orderS;
	private IValidationServices validS;
	private ISpedizioneServices spedS;

	public OrderController(IOrderServices orderS, IValidationServices validS, ISpedizioneServices spedS) {
		this.orderS = orderS;
		this.validS = validS;
		this.spedS = spedS;
	}

	@PostMapping("/init")
	public ResponseEntity<Response> init(@RequestBody (required = true) SpedizioneReq req) {
		Response r = new Response(); 
		HttpStatus status = HttpStatus.OK;
		try {
			spedS.init(req);
			r.setMsg("ok");
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	

	@PostMapping("/createSpedizione")
	public ResponseEntity<Response> createSpedizione(@RequestBody (required = true) SpedizioneReq req) {
		Response r = new Response(); 
		HttpStatus status = HttpStatus.OK;
		try {
			spedS.create(req);
			r.setMsg(validS.getMessaggio("created"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}

	@PutMapping("/updateSpedizione")
	public ResponseEntity<Response> updateSpedizione(@RequestBody (required = true) SpedizioneReq req) {
		Response r = new Response(); 
		HttpStatus status = HttpStatus.OK;
		try {
			spedS.update(req);
			r.setMsg(validS.getMessaggio("updated"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	

	@DeleteMapping("/deleteSpedizione/{id}")
	public ResponseEntity<Response> deleteSpedizione(@PathVariable (required = true) Integer id) {
	    HttpStatus status = HttpStatus.OK;
	    Response response= new Response();
	    try {
	    	spedS.delete(id);
	    	response.setMsg(validS.getMessaggio("deleted"));
	    } catch (Exception e) {
	        status = HttpStatus.BAD_REQUEST;
		    response.setMsg(e.getMessage());	    	        
	    }
	    return ResponseEntity.status(status).body(response);	    	

	}
	

	
	@GetMapping("/listSpedizione")
	public ResponseEntity<Object> listSpedizione(@RequestParam  Integer id){	
		Object r = new Object();
		HttpStatus status = HttpStatus.OK;
		try {
			r=spedS.list(id);
		} catch (Exception e) {
			r=e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
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

	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Response> delete(@PathVariable (required = true) Integer id) {
		Response r = new Response();
		HttpStatus status = HttpStatus.OK;
		try {
			orderS.remove(id);
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
