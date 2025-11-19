package com.betacom.ecommerce.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.ecommerce.dto.input.FamigliaReq;
import com.betacom.ecommerce.dto.output.FamigliaDTO;
import com.betacom.ecommerce.dto.output.ProdottoDTO;
import com.betacom.ecommerce.dto.output.ProdottoFamigliaDTO;
import com.betacom.ecommerce.exception.EcommerceException;
import com.betacom.ecommerce.models.Famiglia;
import com.betacom.ecommerce.models.Prodotto;
import com.betacom.ecommerce.repositories.IFamigliaRepository;
import com.betacom.ecommerce.services.interfaces.IFamigliaServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FamigliaImpl  implements IFamigliaServices{

	private IFamigliaRepository repoF;
	private IValidationServices msgS;
	
	
	public FamigliaImpl(IFamigliaRepository repoF, IValidationServices msgS) {
		this.repoF = repoF;
		this.msgS = msgS;
	}

	@Transactional (rollbackFor = Exception.class)
	@Override
	public void create(FamigliaReq req) throws Exception {
		log.debug("Begin create:" + req);
		
		if (repoF.existsByDescrizioneIgnoreCase(req.getDescrizione().trim()))
			throw new Exception(msgS.getMessaggio("fam_fnd"));
		
		Famiglia f = new Famiglia();
		f.setDescrizione(req.getDescrizione().trim());
		
		repoF.save(f);
		
	}

	@Transactional (rollbackFor = Exception.class)
	@Override
	public void update(FamigliaReq req) throws Exception {
		log.debug("Begin update:" + req);
		
		Famiglia fam = repoF.findById(req.getId())
				.orElseThrow(() ->new Exception(msgS.getMessaggio("fam_ntfnd")));
		
		Optional.ofNullable(req.getDescrizione())
			.map(String::trim)   // trim descrizione
			.ifPresent(desc -> {
				if (!desc.equalsIgnoreCase(fam.getDescrizione())) {
					if (repoF.existsByDescrizioneIgnoreCase(desc))
						throw new EcommerceException(msgS.getMessaggio("fam_fnd"));
				}
				
				fam.setDescrizione(desc);
				repoF.save(fam);
			});		
	}
	
	@Transactional (rollbackFor = Exception.class)
	@Override
	public void delete(Integer id) throws Exception {
		log.debug("Begin delete:" + id);
		
		Famiglia fam = repoF.findById(id)
				.orElseThrow(() -> new Exception(msgS.getMessaggio("fam_ntfnd")));
		
		boolean hasChildren = ((fam.getProdotto() != null) && !fam.getProdotto().isEmpty()) ||
							  ((fam.getArtist() != null) && !fam.getArtist().isEmpty());	
		
		if (hasChildren)
			throw new Exception(msgS.getMessaggio("fam_children"));	
		
		repoF.delete(fam);
	}


	@Override
	public List<FamigliaDTO> list(String pattern) throws Exception {
		log.debug("Begin list:" + pattern);
		List<Famiglia> lF = null;

		lF = (pattern == null || pattern.isBlank()) 
				? repoF.findAll(Sort.by("descrizione").ascending())
				: repoF.findByDescrizioneContainingIgnoreCase(pattern,Sort.by("descrizione").ascending());
		
		return lF.stream()
				.map(f -> FamigliaDTO.builder()
						.id(f.getId())
						.descrizione(f.getDescrizione())
						.build())
				.toList();
	}


	@Override
	public List<ProdottoFamigliaDTO> listPerFamiglia() throws Exception {
		log.debug("Begin listPerFamiglia");
		List<Famiglia> lF = repoF.findAll();
		
		return lF.stream()
				.map(f -> ProdottoFamigliaDTO.builder()
						.id(f.getId())
						.descrizione(f.getDescrizione())
						.prodotto(buildProdottoList(f.getProdotto()))
						.build())
				.toList();						
	}

	@Override
	public ProdottoFamigliaDTO ListByIdProdottoFamiglia(Integer id) throws Exception {
		log.debug("Begin ListByIdProdottoFamiglia :" + id);
		Famiglia fam = repoF.findById(id)
				.orElseThrow(() -> new Exception(msgS.getMessaggio("fam_ntfnd")));
		
		return  ProdottoFamigliaDTO.builder()
				.id(fam.getId())
				.descrizione(fam.getDescrizione())
				.prodotto(buildProdottoList(fam.getProdotto()))
				.build();
	}
	
	private List<ProdottoDTO> buildProdottoList(List<Prodotto> lP){
		return lP.stream()
				.map(p -> ProdottoDTO.builder()
						.id(p.getId())
						.descrizione(p.getDescrizione())
						.famiglia(FamigliaDTO.builder()
								.id(p.getFamiglia().getId())
								.descrizione(p.getFamiglia().getDescrizione())
								.build())
						.build())
				.toList();
	}



}
