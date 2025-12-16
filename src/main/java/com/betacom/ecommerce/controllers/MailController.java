package com.betacom.ecommerce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.ecommerce.dto.input.MailReq;
import com.betacom.ecommerce.response.Response;
import com.betacom.ecommerce.services.interfaces.IMailServices;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("rest/mail")
public class MailController {
	
	private final IMailServices mailS;
	
	
	@PostMapping("/send")
	public ResponseEntity<Response<String, Boolean>> send(@RequestBody (required = true) MailReq req) {
		Response<String, Boolean> r = new Response<String, Boolean>(); 
		HttpStatus status = HttpStatus.OK;
		try {
			mailS.sendMail(req);
			r.setMsg("OK");
		} catch (Exception e) {
			r.setMsg(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(status).body(r);
	}
}
