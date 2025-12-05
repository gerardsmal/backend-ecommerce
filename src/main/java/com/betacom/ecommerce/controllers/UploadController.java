package com.betacom.ecommerce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.betacom.ecommerce.response.Response;
import com.betacom.ecommerce.services.interfaces.IUploadServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

@RestController
@RequestMapping("/rest/upload")
public class UploadController {

	
	private final IUploadServices uplS;
	private final IValidationServices valS;
	
	 
//	@Value("${app.images.url-prefix:/images}")
//	private String imagesUrlPrefix;

	public UploadController(IUploadServices uplS, IValidationServices valS) {
		this.uplS = uplS;
		this.valS = valS;
	}
	 
	@PostMapping(value = "/image", consumes = "multipart/form-data")
	public ResponseEntity<Response<String, Boolean>> uploadImage(
			@RequestParam MultipartFile file,
			@RequestParam Integer id) {
		Response<String, Boolean> r = new Response<String, Boolean>();
		HttpStatus status = HttpStatus.OK;
		try {
			 /*
			   Test del content type:
						PNG	image/png
						JPG	image/jpeg
						GIF	image/gif
			  */
			 if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
				 	r.setMsg(valS.getMessaggio("upload_invalid"));
	                return ResponseEntity.badRequest().body(r);	            
			 }
			 
			 r.setMsg(uplS.saveImage(file, id));
			 
			 return ResponseEntity.status(HttpStatus.CREATED).body(r);
			 
		 } catch (Exception e) {
			 r.setMsg(e.getMessage());
			 return ResponseEntity.internalServerError().body(r);
		 }
	 }
	
	@GetMapping("getUrl")
	public ResponseEntity<Response<String, Boolean>> getUrl(@RequestParam (required = true) String filename) {
		Response<String, Boolean> r = new Response<String, Boolean>();
		HttpStatus status = HttpStatus.OK;
		
		r.setMsg(uplS.buildUrl(filename));
		return ResponseEntity.status(status).body(r);
	}
}
