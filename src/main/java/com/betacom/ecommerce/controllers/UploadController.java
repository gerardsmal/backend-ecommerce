package com.betacom.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.betacom.ecommerce.services.interfaces.IUploadServices;

@RestController
@RequestMapping("/rest/upload")
public class UploadController {

	
	private final IUploadServices uplS;
	
	 
	@Value("${app.images.url-prefix:/images}")
	private String imagesUrlPrefix;

	public UploadController(IUploadServices uplS) {
		this.uplS = uplS;
	}
	 
	@PostMapping(value = "/image", consumes = "multipart/form-data")
	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
		try {
			 /*
			   Test del content type:
						PNG	image/png
						JPG	image/jpeg
						GIF	image/gif
			  */
			 if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
	                return ResponseEntity.badRequest().body("Il file deve essere un'immagine");	            
			 }
			 
			 String filename = uplS.saveImage(file);
			 
			 String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()  // recupera la parte iniziale dell URL // localhost:8080/
	                    .path(imagesUrlPrefix + "/")    // il prefisse sarebbe image
	                    .path(filename)                 // il nome del file
	                    .toUriString();
			 
			 return ResponseEntity.status(HttpStatus.CREATED).body(fileUrl);
			 
		 } catch (Exception e) {
			 return ResponseEntity.internalServerError().body("Errore: " + e.getMessage());
		 }
	 }

}
