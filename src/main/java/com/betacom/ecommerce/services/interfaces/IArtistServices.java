package com.betacom.ecommerce.services.interfaces;

import java.util.List;

import com.betacom.ecommerce.dto.input.ArtistReq;
import com.betacom.ecommerce.dto.input.ChangeFamilyReq;
import com.betacom.ecommerce.dto.output.ArtistaDTO;
import com.betacom.ecommerce.dto.output.ArtistaWebDTO;

public interface IArtistServices {
	void create(ArtistReq req) throws Exception;
	void update(ArtistReq req) throws Exception;
	void remove(Integer id) throws Exception;
	
	void removeFamigliaArtist(Integer id, Integer famiglia) throws Exception;
	void changeFamily(ChangeFamilyReq req) throws Exception;
	
	ArtistaDTO listByArtista(Integer id) throws Exception;
	List<ArtistaDTO> list(String nome, Integer famiglia) throws Exception;
	List<ArtistaWebDTO> listWeb(String nome, Integer famiglia) throws Exception;
	

	
}
