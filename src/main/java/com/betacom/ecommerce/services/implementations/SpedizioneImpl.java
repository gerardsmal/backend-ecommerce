package com.betacom.ecommerce.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.ecommerce.dto.input.SpedizioneReq;
import com.betacom.ecommerce.dto.output.SpedizioneDTO;
import com.betacom.ecommerce.models.Account;
import com.betacom.ecommerce.models.Spedizione;
import com.betacom.ecommerce.repositories.IAccountRepository;
import com.betacom.ecommerce.repositories.ISpedizioneRepository;
import com.betacom.ecommerce.services.interfaces.ISpedizioneServices;
import com.betacom.ecommerce.services.interfaces.IValidationServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class SpedizioneImpl implements ISpedizioneServices{

    private final ValidationImpl validationImpl;	
	private final IAccountRepository accR;
	private final ISpedizioneRepository spedR;
	private final IValidationServices validS;
	
	private static String capRegex = "^[0-9]{5}$";
	

	@Transactional (rollbackFor = Exception.class)
	@Override
	public void init(SpedizioneReq req) throws Exception {
		log.debug("init: {}", req);
		Account account = accR.findById(req.getAccountId())
				.orElseThrow(() -> new Exception(validS.getMessaggio("account_ntfnd")));
		
		if (account.getSpedizione().isEmpty()) {
			spedR.save(createSpedizione(SpedizioneReq.builder()
					.isDefault(true)
					.cognome(account.getCognome())
					.nome(account.getNome())
					.cap(account.getCap())
					.via(account.getVia())
					.commune(account.getCommune())
					.build(), account));			
			
		}
		
		
	}

	@Transactional (rollbackFor = Exception.class)
	@Override
	public void create(SpedizioneReq req) throws Exception {
		log.debug("create {}", req);
		Account account = accR.findById(req.getAccountId())
				.orElseThrow(() -> new Exception(validS.getMessaggio("account_ntfnd")));
		
		validS.checkNotNull(req.getNome(), "account_no_nome");
		validS.checkNotNull(req.getCognome(), "account_no_cognome");
		validS.checkNotNull(req.getCommune(), "account_no_comune");
		validS.checkNotNull(req.getVia(), "account_no_via");
		validS.validateWithRegex(req.getCap(), capRegex, "account_cap_ko");
		
		spedR.save(createSpedizione(SpedizioneReq.builder()
				.isDefault(false)
				.cognome(req.getCognome())
				.nome(req.getNome())
				.cap(req.getCap())
				.via(req.getVia())
				.commune(req.getCommune())
				.build(), account));

	}


	
	
	private Spedizione createSpedizione(SpedizioneReq req, Account acc) {
		log.debug("createSpedizione {}", req);
		Spedizione spd = new Spedizione();
		spd.setPredefinito(req.getIsDefault());
		spd.setAccount(acc);
		spd.setNome(req.getNome());
		spd.setCognome(req.getCognome());
		spd.setCommune(req.getCommune());
		spd.setVia(req.getVia());
		spd.setCap(req.getCap());
		
		return spd;
	}
	
	@Transactional (rollbackFor = Exception.class)
	@Override
	public void update(SpedizioneReq req) throws Exception {
		log.debug("updateSpedizione {}", req);
		
		Spedizione sped = spedR.findById(req.getId())
				.orElseThrow(() -> new Exception(validS.getMessaggio("spedizione_ntfnd")));
		
		Optional.ofNullable(req.getNome()).ifPresent(sped::setNome);
		Optional.ofNullable(req.getCognome()).ifPresent(sped::setCognome);
		Optional.ofNullable(req.getCommune()).ifPresent(sped::setCommune);
		Optional.ofNullable(req.getVia()).ifPresent(sped::setVia);

		Optional.ofNullable(req.getCap())
			.ifPresent(cap -> {
				validS.validateWithRegex(cap, capRegex, "account_cap_ko");
				sped.setCap(cap);
			});

		spedR.save(sped);
		

	}

	@Transactional (rollbackFor = Exception.class)
	@Override
	public void delete(Integer spedizioneID) throws Exception {
		log.debug("delete {}", spedizioneID);

		Spedizione sped = spedR.findById(spedizioneID)
				.orElseThrow(() -> new Exception(validS.getMessaggio("spedizione_ntfnd")));

		spedR.delete(sped);
	}

	
	
	
	@Override
	public List<SpedizioneDTO> list(Integer id) throws Exception {
		log.debug("list : {}", id);
		Account account = accR.findById(id)
				.orElseThrow(() -> new Exception(validS.getMessaggio("account_ntfnd")));
		
		
		return account.getSpedizione().stream()
				.map((s -> SpedizioneDTO.builder()
						.isDefault(s.getPredefinito())
						.id(s.getId())
						.nome(s.getNome())
						.cognome(s.getCognome())
						.via(s.getVia())
						.commune(s.getCommune())
						.cap(s.getCap())
						.build()
						))
				.toList();
	}

	@Override
	public SpedizioneDTO getById(Integer spedizioneID) throws Exception {
		log.debug("getById: {}", spedizioneID);
		Spedizione s = spedR.findById(spedizioneID)
				.orElseThrow(() -> new Exception(validS.getMessaggio("spedizione_ntfnd")));
		
		return SpedizioneDTO.builder()
				.isDefault(s.getPredefinito())
				.id(s.getId())
				.nome(s.getNome())
				.cognome(s.getCognome())
				.via(s.getVia())
				.commune(s.getCommune())
				.cap(s.getCap())
				.build();
	}

}
