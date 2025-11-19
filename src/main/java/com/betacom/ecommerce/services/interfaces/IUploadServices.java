package com.betacom.ecommerce.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadServices {
	
	String saveImage(MultipartFile file) throws Exception;
}
