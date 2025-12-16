package com.betacom.ecommerce.services.implementations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.betacom.ecommerce.dto.input.MailReq;
import com.betacom.ecommerce.services.interfaces.IMailServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailImpl implements IMailServices{
	
	@Value("${mailSender}")
	private String from;
	
	
	private final JavaMailSender mailSender;
	private final IValidationServices validS;

	@Override
	@Async
	public void sendMail(MailReq req) throws Exception {
		log.debug("sendMail; {}", req);
		
		if (req.getTo() == null || req.getOggetto() == null || req.getBody() == null)
			throw new Exception(validS.getMessaggio("mail_error"));
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true, "UTF-8");
		
		helper.setTo(req.getTo());
		helper.setFrom(from);
		helper.setSubject(req.getOggetto());
		helper.setText(req.getBody());
		log.debug("prima del send");
		mailSender.send(mimeMessage);
		log.debug("dopo  send");
		
		
	}
}
