package com.betacom.ecommerce.services.implementations;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.betacom.ecommerce.services.interfaces.IUploadServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

@Service
public class UploadImpl implements IUploadServices{
	
	private final Path uploadPath;
	private final IValidationServices validS;
	
	public UploadImpl(@Value("${app.upload.dir:uploads}") String uploadDir,
			IValidationServices validS) {  // valore per default della value
	        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize(); // transform relative path in absolute  path
	        this.validS = validS;
	        init();
	    }	
	
	
	  private void init() {
        try {
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException(validS.getMessaggio("upload_empty"));
        }
    }
	
	@Override
	public String saveImage(MultipartFile file) throws Exception {
		
		Assert.isTrue(!file.isEmpty(), validS.getMessaggio("upload_empty"));
		
        // Estensione (es. ".png")
        String originalName = file.getOriginalFilename();
        String extension = "";

        extension = Optional.ofNullable(originalName)         // search extension file 
                .filter(name -> name.contains("."))
                .map(name -> name.substring(name.lastIndexOf(".")))
                .orElse("");

        // Nome univoco
        String uniqueName = UUID.randomUUID().toString() + extension;

        Path destinationFile = uploadPath.resolve(uniqueName);

        try {
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(validS.getMessaggio("upload_save_error"));
        }

        // Ritorno solo il nome per costruire l’URL
	        return uniqueName;
	}

}
