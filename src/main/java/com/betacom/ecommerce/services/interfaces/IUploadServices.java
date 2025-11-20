package com.betacom.ecommerce.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import com.betacom.ecommerce.exception.EcommerceException;

public interface IUploadServices {
	
	String saveImage(MultipartFile file, Integer id) throws Exception;
	
	void removeImage(String filename) throws EcommerceException;
	String buildUrl(String filename);
	
}
