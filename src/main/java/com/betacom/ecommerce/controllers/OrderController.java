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
import com.betacom.ecommerce.dto.output.OrderDTO;
import com.betacom.ecommerce.response.Response;
import com.betacom.ecommerce.services.interfaces.IOrderServices;
import com.betacom.ecommerce.services.interfaces.ISpedizioneServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("rest/order")
public class OrderController {

	private final IOrderServices orderS;
	private final IValidationServices validS;
	private final ISpedizioneServices spedS;


	@PostMapping("/init")
	public ResponseEntity<Response<String, Boolean>> init(@RequestBody (required = true) SpedizioneReq req) {
		Response<String, Boolean> r = new Response<String, Boolean>(); 
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
	public ResponseEntity<Response<String, Boolean>> createSpedizione(@RequestBody (required = true) SpedizioneReq req) {
		Response<String, Boolean> r = new Response<String, Boolean>(); 
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
	public ResponseEntity<Response<String, Boolean>> updateSpedizione(@RequestBody (required = true) SpedizioneReq req) {
		Response<String, Boolean> r = new Response<String, Boolean>(); 
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
	public ResponseEntity<Response<String, Boolean>> deleteSpedizione(@PathVariable (required = true) Integer id) {
	    HttpStatus status = HttpStatus.OK;
	    Response<String, Boolean> response= new Response<String, Boolean>();
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
	
	@GetMapping("/getSpedizione")
	public ResponseEntity<Object> getAccount( @RequestParam (required = false) Integer id){	
		
		HttpStatus status = HttpStatus.OK;
		try {		
			return ResponseEntity.status(status).body(spedS.getById(id));
		} catch (Exception e) {
			Response<String, Boolean> r = new Response<String, Boolean>();
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
			return ResponseEntity.status(status).body(r);
		}
	}

	@GetMapping("orderStatus")
	public ResponseEntity<Object> orderStatus(@RequestParam (required = true) Integer id) {
		HttpStatus status = HttpStatus.OK;
		try {
			Response<Boolean, Boolean> r = new Response<Boolean, Boolean>();
			r.setMsg(orderS.getOrderStatus(id));
			return ResponseEntity.status(status).body(r);
		} catch (Exception e) {
			Response<String, Boolean> r = new Response<String, Boolean>();
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
			return ResponseEntity.status(status).body(r);
		}
		
	}
	
	@PostMapping("/create")
	public ResponseEntity<Response<String, Boolean>> create(@RequestBody (required = true) OrderReq req) {
		Response<String, Boolean> r = new Response<String, Boolean>(); 
		HttpStatus status = HttpStatus.OK;
		try {
			orderS.create(req);
			r.setMsg(validS.getMessaggio("order_created"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}

	@PutMapping("/confirm")
	public ResponseEntity<Object> confirm(@RequestBody (required = true) OrderReq req) {

		HttpStatus status = HttpStatus.OK;
		try {
			OrderDTO r = orderS.confirm(req);
			return ResponseEntity.status(status).body(r);
		} catch (Exception e) {
			Response<String, Boolean> r = new Response<String, Boolean>(); 
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
			return ResponseEntity.status(status).body(r);
		}
	
	}

	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Response<String, Boolean>> delete(@PathVariable (required = true) Integer id) {
		Response<String, Boolean> r = new Response<String, Boolean>();
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
	
	@GetMapping("/lastOrder")
	public ResponseEntity<Object> lastOrder(@RequestParam (required = true) Integer id){	
		Object r = new Object();
		HttpStatus status = HttpStatus.OK;
		try {
			r=orderS.getLastOrdine(id);
		} catch (Exception e) {
			r=e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
}
