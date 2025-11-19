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

import com.betacom.ecommerce.dto.input.ArtistReq;
import com.betacom.ecommerce.dto.input.ChangeFamilyReq;
import com.betacom.ecommerce.response.Response;
import com.betacom.ecommerce.services.interfaces.IArtistServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

@RestController
@RequestMapping("rest/artist")
public class ArtistController {

	private IArtistServices artS;
	private IValidationServices validS;

	public ArtistController(IArtistServices artS,IValidationServices validS) {
		this.artS = artS;
		this.validS = validS;
	}

	@PostMapping("/create")
	public ResponseEntity<Response> create(@RequestBody (required = true) ArtistReq req) {
		Response r = new Response();
		HttpStatus status = HttpStatus.OK;
		try {
			artS.create(req);
			r.setMsg(validS.getMessaggio("created"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Response> update(@RequestBody (required = true) ArtistReq req) {
		Response r = new Response();
		HttpStatus status = HttpStatus.OK;
		try {
			artS.update(req);
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
			artS.remove(id);
			r.setMsg(validS.getMessaggio("deleted"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}

	
	
	@DeleteMapping("/removeFamiglia/{id}/{famiglia}")
	public ResponseEntity<Response> removeFamiglia(@PathVariable (required = true) Integer id, 
				@PathVariable (required = true) Integer famiglia) {
		Response r = new Response();
		HttpStatus status = HttpStatus.OK;
		try {
			artS.removeFamigliaArtist(id, famiglia);
			r.setMsg(validS.getMessaggio("deleted"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}	
		return ResponseEntity.status(status).body(r);
	}

	
	@PutMapping("/changeFamily")
	public ResponseEntity<Response> changeFamily(@RequestBody (required = true) ChangeFamilyReq req) {
		Response r = new Response();
		HttpStatus status = HttpStatus.OK;
		try {
			artS.changeFamily(req);
			r.setMsg(validS.getMessaggio("updated"));
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
	
	@GetMapping("/listArtistbyId")
	public ResponseEntity<Object> listArtistbyId(@RequestParam (required = true) Integer id){
		Object r = new Object();
		HttpStatus status = HttpStatus.OK;
		try {
			r = artS.listByArtista(id);
		} catch (Exception e) {
			r = e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}

	@GetMapping("/list")
	public ResponseEntity<Object> list(
			@RequestParam (required = false) String nome,
			@RequestParam (required = false) Integer famiglia){
		Object r = new Object();
		HttpStatus status = HttpStatus.OK;
		try {
			r= artS.list(nome, famiglia);
		} catch (Exception e) {
			r=e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}

	@GetMapping("/listWeb")
	public ResponseEntity<Object> listWeb(
			@RequestParam (required = false) String nome,
			@RequestParam (required = false) Integer famiglia){
		Object r = new Object();
		HttpStatus status = HttpStatus.OK;
		try {
			r= artS.listWeb(nome, famiglia);
		} catch (Exception e) {
			r=e.getMessage();
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}

}
