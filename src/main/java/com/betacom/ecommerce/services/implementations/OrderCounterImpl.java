package com.betacom.ecommerce.services.implementations;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.ecommerce.models.OrderCounter;
import com.betacom.ecommerce.repositories.IOrderCounterRepository;
import com.betacom.ecommerce.services.interfaces.IOrderCounterServices;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderCounterImpl implements IOrderCounterServices{

	private final IOrderCounterRepository countR;
	

	@Transactional (rollbackFor = Exception.class)
	@Override
	public Long nextOrderNumber() throws Exception {
		OrderCounter counter = countR.findById(1)
				.orElseThrow(() -> new Exception("Counter table not initilized.."));
		
		Long next = counter.getCurrentValue() + 1;
		counter.setCurrentValue(next);
		countR.save(counter);
		
		
		return next;
	}

}
