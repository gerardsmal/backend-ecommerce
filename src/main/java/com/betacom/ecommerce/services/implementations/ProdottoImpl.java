package com.betacom.ecommerce.services.implementations;

import static com.betacom.ecommerce.utils.Utilities.buildFamigliaDTOList;
import static com.betacom.ecommerce.utils.Utilities.buildPrezzoDTOList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.ecommerce.dto.input.ProdottoReq;
import com.betacom.ecommerce.dto.output.ArtistaDTO;
import com.betacom.ecommerce.dto.output.FamigliaDTO;
import com.betacom.ecommerce.dto.output.ProdottoDTO;
import com.betacom.ecommerce.exception.EcommerceException;
import com.betacom.ecommerce.models.Artist;
import com.betacom.ecommerce.models.Famiglia;
import com.betacom.ecommerce.models.Prezzo;
import com.betacom.ecommerce.models.Prodotto;
import com.betacom.ecommerce.repositories.IArtistRepository;
import com.betacom.ecommerce.repositories.IFamigliaRepository;
import com.betacom.ecommerce.repositories.IProdottoRepository;
import com.betacom.ecommerce.services.interfaces.IProdottoServices;
import com.betacom.ecommerce.services.interfaces.IUploadServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProdottoImpl implements IProdottoServices{

	private final IProdottoRepository repP;
	private final IArtistRepository   artistR;
	private final IValidationServices validS;
	private final IUploadServices     uploadS;


	@Transactional (rollbackFor =  Exception.class)	
	@Override
	public Integer create(ProdottoReq req) throws Exception {
	
		log.debug("Begin create:" + req);
		validS.checkNotNull(req.getDescrizione(), "prod_no_desc");

		Optional<Prodotto> prod = repP.findByDescrizione(req.getDescrizione().trim());
		if (prod.isPresent())
			throw new Exception(validS.getMessaggio("prod_fnd"));
		
		validS.checkNotNull(req.getIdFamiglia(), "prod_no_famiglia");
		validS.checkNotNull(req.getIdArtist(), "prod_no_artist");
		
		Artist artist = artistR.findById(req.getIdArtist())
				.orElseThrow(() -> new Exception(validS.getMessaggio("artist_ntfnd")));
	
		Famiglia fam = controlFamiglia(artist.getFamiglia(), req.getIdFamiglia());
		
		Prodotto p = new Prodotto();
		p.setDescrizione(req.getDescrizione().trim());
		p.setFamiglia(fam);
		p.setArtista(artist);
		
		return repP.save(p).getId();
		
	}

	@Transactional (rollbackFor =  Exception.class)
	@Override
	public void update(ProdottoReq req) throws Exception {
		log.debug("Begin update:" + req);
		Prodotto prod = repP.findById(req.getId())
				.orElseThrow(() -> new Exception(validS.getMessaggio("prod_ntfnd")));
		
		Optional.ofNullable(req.getDescrizione()).ifPresent(prod::setDescrizione);
				
		if (req.getIdArtist() != null) {
			Artist artist = artistR.findById(req.getIdArtist())
					.orElseThrow(() -> new Exception(validS.getMessaggio("artist_ntfnd")));
			prod.setArtista(artist);			
		}
		
		
		Optional.ofNullable(req.getIdFamiglia())
			.ifPresent(idFamiglia -> {
				prod.setFamiglia(controlFamiglia(prod.getArtista().getFamiglia(), idFamiglia));
			});
		
		repP.save(prod);
	}

	@Transactional (rollbackFor = Exception.class)
	@Override
	public void delete(Integer id) throws Exception {
		log.debug("Begin delete:" + id);
	
		
		Prodotto prod = repP.findById(id)
				.orElseThrow(() -> new Exception(validS.getMessaggio("prod_ntfnd")));
		
		if (!prod.getRigaCarello().isEmpty())
			throw new Exception(validS.getMessaggio("prod_carello_fnd"));
		
		Optional.ofNullable(prod.getImage())
			.ifPresent(image -> {
				uploadS.removeImage(image);
			});
		
		repP.delete(prod);
		
	}

	

	@Override
	public List<ProdottoDTO> list(Integer id, String desc, Integer artist, Integer famiglia) throws Exception {
		if (desc != null) desc = desc.toUpperCase();
		log.debug("list:" + id + "/" + desc + "/" + artist + "/" + famiglia);
		List<Prodotto> lP = repP.searchByFilter(id, desc, artist, famiglia);
		log.debug("prodotti trovati:" + lP.size());
		return lP.stream()
				.map(p -> ProdottoDTO.builder()
						.id(p.getId())
						.descrizione(p.getDescrizione())
						.image(p.getImage() == null ? null : uploadS.buildUrl(p.getImage()))
						.famiglia(FamigliaDTO.builder()
								.id(p.getFamiglia().getId())
								.descrizione(p.getFamiglia().getDescrizione())
								.build())
						.artista(ArtistaDTO.builder()
								.id(p.getArtista().getId())
								.nome(p.getArtista().getNome())
								.famiglia(buildFamigliaDTOList(p.getArtista().getFamiglia()))
								.build()
								)
						.supports(buildSupports(p.getPrezzo()))
						.prezzo(buildPrezzoDTOList(p.getPrezzo()))
						.build()
						)
						
				.toList();
		
	}
	@Override
	public ProdottoDTO getById(Integer id) throws Exception {
		log.debug("getById:" + id);
		
		Prodotto p = repP.findById(id)
				.orElseThrow(() -> new Exception(validS.getMessaggio("prod_ntfnd")));
		
		return ProdottoDTO.builder()
				.id(p.getId())
				.descrizione(p.getDescrizione())
				.image(p.getImage() == null ? null : uploadS.buildUrl(p.getImage()))
				.famiglia(FamigliaDTO.builder()
						.id(p.getFamiglia().getId())
						.descrizione(p.getFamiglia().getDescrizione())
						.build())
				.artista(ArtistaDTO.builder()
						.id(p.getArtista().getId())
						.nome(p.getArtista().getNome())
						.famiglia(buildFamigliaDTOList(p.getArtista().getFamiglia()))
						.build()
						)
				.prezzo(buildPrezzoDTOList(p.getPrezzo()))
				.build();
				
	}
	
	
	private String buildSupports(List<Prezzo> lP) {
		return lP.stream()
				.map(pr -> pr.getSupporto().toString() )
				.collect(Collectors.joining(", "));
	}
	/*
	 * control family validity
	 * family must be compatible with original artist family
	 */
	Famiglia controlFamiglia(List<Famiglia> lF, Integer idFamiglia) throws EcommerceException{
		Famiglia fam = lF.stream()
			    .filter(f -> f.getId() == idFamiglia)
			    .findFirst()
			    .orElseThrow(() -> new EcommerceException(validS.getMessaggio("prod_fam.incomp")));
		return fam;
	}


	

}
