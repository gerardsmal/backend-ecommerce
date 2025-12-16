package com.betacom.ecommerce.services.interfaces;

import com.betacom.ecommerce.dto.input.MailReq;

public interface IMailServices {

	void sendMail(MailReq req) throws Exception;
	void sendMailWithExcel(MailReq req) throws Exception;
}
