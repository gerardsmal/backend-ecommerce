package com.betacom.ecommerce.services.implementations;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.betacom.ecommerce.exception.EcommerceException;
import com.betacom.ecommerce.models.Prodotto;
import com.betacom.ecommerce.repositories.IProdottoRepository;
import com.betacom.ecommerce.services.interfaces.IUploadServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UploadImpl implements IUploadServices{
	
	private final Path uploadPath;
	private final IValidationServices validS;
	private final IProdottoRepository prodR;
	
	public UploadImpl(@Value("${app.upload.dir:uploads}") String uploadDir,
			IValidationServices validS,
			IProdottoRepository prodR) {  // valore per default della value
	        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize(); // transform relative path in absolute  path
	        this.validS = validS;
	        this.prodR = prodR;
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
	
	@Transactional (rollbackFor = Exception.class)
	@Override
	public String saveImage(MultipartFile file, Integer id) throws Exception {
		log.debug("saveImage:" + id);
		
		Assert.isTrue(!file.isEmpty(),() -> validS.getMessaggio("upload_empty"));
		
        // Estensione (es. ".png")
        String original = file.getOriginalFilename();
        String extension = "";
        String originalName = original.trim().replaceAll("\\s+", "_");
        
        log.debug("originalName:" + originalName);
        
        extension = Optional.ofNullable(originalName)         // search extension file 
                .filter(name -> name.contains("."))
                .map(name -> name.substring(name.lastIndexOf(".")))
                .orElse("");

        // Nome univoco
        String uniqueName =  originalName.substring(0, originalName.lastIndexOf(".")) + "-" +  UUID.randomUUID().toString() + extension;

        Path destinationFile = uploadPath.resolve(uniqueName);

        try {
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            Prodotto prod = prodR.findById(id)
            		.orElseThrow(() -> new Exception(validS.getMessaggio("prod_ntfnd")));
            
            Optional.ofNullable(prod.getImage())
            .ifPresent(imageName -> removeImage(imageName));

            prod.setImage(uniqueName);
            prodR.save(prod);
            
        } catch (IOException e) {
            throw new RuntimeException(validS.getMessaggio("upload_save_error"));
        }
    
        return uniqueName;
	}


	@Override
	public void removeImage(String filename) throws EcommerceException {
		Path updalofPath = Paths.get("uploads").toAbsolutePath().normalize();
		Path filpath = updalofPath.resolve(filename);
		if (Files.exists(filpath)){
			log.debug("Remove image:" + filpath);
			try {
				Files.delete(filpath);
				log.debug("Images deleted..");
			} catch (Exception e) {
				throw new EcommerceException(e.getMessage());
			}
		}

	}


	@Override
	public String buildUrl(String filename) {
		return ServletUriComponentsBuilder.fromCurrentContextPath()  // recupera la parte iniziale dell URL // localhost:8080/
                .path("/images/")    // il prefisse sarebbe image
                .path(filename)                 // il nome del file
                .toUriString(); 	
	
	}

}
