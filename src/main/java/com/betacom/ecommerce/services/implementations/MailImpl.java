package com.betacom.ecommerce.services.implementations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
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
		helper.setFrom(from, "DisciShop");
		helper.setReplyTo("noreply@discishop.it");

		helper.setSubject(req.getOggetto());
		helper.setText(req.getBody(), true);

		helper.addInline(
			    "logo",
			    new ClassPathResource("static/logo.png"),
			    "image/png"
			);

		mailSender.send(mimeMessage);
		log.debug("dopo  send");
		
		
	}

	@Override
	public void sendMailWithExcel(MailReq req) throws Exception {
		log.debug("sendMailWithAttachment :" + req.getTo());
		
		if (req.getTo() == null || req.getOggetto() == null || req.getBody() == null || req.getAttachment() == null)
			throw new Exception(validS.getMessaggio("mail_attachment_error"));
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true, "UTF-8");

		helper.setTo(req.getTo());
		helper.setFrom(from, "DisciShop");
		helper.setReplyTo("noreply@discoshop.it");
		helper.setSubject(req.getOggetto());
		helper.setText(req.getBody(), true);

		helper.addInline(
			    "logo",
			    new ClassPathResource("static/logo.png"),
			    "image/png"
			);

		
		helper.addAttachment("ordine.xlsx", 
				new ByteArrayResource(req.getAttachment()), 
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		
		log.debug("prima send");
		mailSender.send(mimeMessage);
		log.debug("Dopo send");
	}
}
