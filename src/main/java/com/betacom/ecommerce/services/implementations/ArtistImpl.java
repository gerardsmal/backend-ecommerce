package com.betacom.ecommerce.services.implementations;

import static com.betacom.ecommerce.utils.Utilities.buildFamigliaDTOList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.ecommerce.dto.input.ArtistReq;
import com.betacom.ecommerce.dto.input.ChangeFamilyReq;
import com.betacom.ecommerce.dto.output.ArtistaDTO;
import com.betacom.ecommerce.dto.output.ArtistaWebDTO;
import com.betacom.ecommerce.exception.EcommerceException;
import com.betacom.ecommerce.models.Artist;
import com.betacom.ecommerce.models.Famiglia;
import com.betacom.ecommerce.repositories.IArtistRepository;
import com.betacom.ecommerce.repositories.IFamigliaRepository;
import com.betacom.ecommerce.services.interfaces.IArtistServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArtistImpl implements IArtistServices{

	private IArtistRepository artS;
	private IFamigliaRepository famS;
	private IValidationServices  validS;
	
	public ArtistImpl(IArtistRepository artS, IFamigliaRepository famS,IValidationServices  msgS) {
		this.artS = artS;
		this.famS = famS;
		this.validS = msgS;
	}

	
	@Transactional (rollbackFor = Exception.class)
	@Override
	public void create(ArtistReq req) throws Exception {
		log.debug("create:" + req);
		validS.checkNotNull(req.getNome(), "artist_no_name");
		
		if ( artS.existsByNome(req.getNome().trim())) {
			throw new Exception(validS.getMessaggio("artist_fnd"));
		} 
			
		Artist artist = new Artist();
		artist.setNome(req.getNome().trim());
		if (req.getIdFamiglia() != null) {
			Famiglia fam = famS.findById(req.getIdFamiglia())
					.orElseThrow(() -> new Exception(validS.getMessaggio("fam_ntfnd")));
			artist.setFamiglia(new ArrayList<>()); // init famiglia
			artist.getFamiglia().add(fam);
		}
		
		artS.save(artist);
		
	}
	
	@Transactional (rollbackFor = Exception.class)
	@Override
	public void update(ArtistReq req) throws Exception {
		log.debug("update:" + req);
		Artist artist = artS.findById(req.getId())
				.orElseThrow(() -> new Exception(validS.getMessaggio("artist_ntfnd")));
		
		Optional.ofNullable(req.getNome())
			.ifPresent(nome -> {
				if (!nome.equalsIgnoreCase(artist.getNome())) {
					if (artS.existsByNome(nome.trim()))
						throw new EcommerceException(validS.getMessaggio("artist_fnd"));
					artist.setNome(nome);					
				}
			});
	
		// remove family if exist 
		artist.getFamiglia().stream()
			.filter(f -> Objects.equals(f.getId(), req.getIdFamiglia()))
			.findFirst()
			.ifPresent(f -> artist.getFamiglia().remove(f));

		Optional.ofNullable(req.getIdFamiglia())
			.ifPresent(idFamiglia -> {
				Famiglia fam = famS.findById(idFamiglia)
						.orElseThrow(() -> new EcommerceException(validS.getMessaggio("fam_ntfnd")));
				artist.getFamiglia().add(fam);								
			});

		artS.save(artist);	
		
		
	}

	@Transactional (rollbackFor = Exception.class)
	@Override
	public void removeFamigliaArtist(Integer id, Integer idFamiglia) throws Exception {
		log.debug("remove:" + id + "/" + idFamiglia);
		Artist ar = artS.findById(id)
				.orElseThrow(() -> new Exception(validS.getMessaggio("artist_ntfnd")));
		
		int size = ar.getFamiglia().size(); 
		
		ar.getFamiglia().stream()
			.filter(f -> Objects.equals(f.getId(), idFamiglia))
			.findFirst()
			.ifPresent(f -> ar.getFamiglia().remove(f));
		
		Optional.ofNullable(size)
			.filter(s -> s != ar.getFamiglia().size())
			.orElseThrow(() -> new Exception(validS.getMessaggio("artist-no-fam")));
				
		artS.save(ar);
		
	}

	@Transactional (rollbackFor = Exception.class)
	@Override
	public void changeFamily(ChangeFamilyReq req) throws Exception {
		log.debug("changeFamily:" + req);
		removeFamigliaArtist(req.getId(), req.getIdFamiglia());
		log.debug("After remove Family....");
		
		ArtistReq reqInterne = new ArtistReq();
		reqInterne.setId(req.getId());		
		reqInterne.setIdFamiglia(req.getNewIdFamiglia());
		update(reqInterne);
		log.debug("After Update Family....");

	}

	@Transactional (rollbackFor = Exception.class)
	@Override
	public void remove(Integer id) throws Exception {
		log.debug("remove:" + id);
		Artist ar = artS.findById(id)
				.orElseThrow(() -> new Exception(validS.getMessaggio("artist_ntfnd")));
	
		if (!ar.getProdotto().isEmpty())
			throw new Exception(validS.getMessaggio("artist-prod_fnd"));
		artS.delete(ar);
		
	}



	@Override
	public ArtistaDTO listByArtista(Integer id) throws Exception {
		log.debug("listByArtista:" + id);
		
		Artist art = artS.findById(id)
				.orElseThrow(() -> new Exception(validS.getMessaggio("artist_ntfnd")));
	
		
		return ArtistaDTO.builder()
				.id(art.getId())
				.nome(art.getNome())
				.famiglia(buildFamigliaDTOList(art.getFamiglia()))
				.build();
	}


	@Override
	public List<ArtistaDTO> list(String nome, Integer famiglia) throws Exception {
		log.debug("list:" + nome +"/" + famiglia);
		
		
		List<Artist> lA = artS.searchByFilter(nome, famiglia);
		
		return lA.stream()
				.map(art -> ArtistaDTO.builder()
						.id(art.getId())
						.nome(art.getNome())
						.famiglia(buildFamigliaDTOList(art.getFamiglia()))
						.build()
						)
				.toList();
	}


	@Override
	public List<ArtistaWebDTO> listWeb(String nome, Integer famiglia) throws Exception {
		log.debug("list:" + nome +"/" + famiglia);
		
		List<Artist> lA = artS.searchByFilter(nome, famiglia);

		
		return lA.stream()
				.map(art -> ArtistaWebDTO.builder()
						.id(art.getId())
						.nome(art.getNome())
						.families(buildFamilies(art.getFamiglia()))
						.build()						
						).toList();
	}

	private String buildFamilies(List<Famiglia> lF) {
		return lF.stream()
				.map(Famiglia::getDescrizione )
				.collect(Collectors.joining(", "));

	}
	
}
