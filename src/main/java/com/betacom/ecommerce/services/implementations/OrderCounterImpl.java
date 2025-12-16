package com.betacom.ecommerce.services.implementations;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.ecommerce.models.OrderCounter;
import com.betacom.ecommerce.repositories.IOrderCounterRepository;
import com.betacom.ecommerce.services.interfaces.IOrderCounterServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderCounterImpl implements IOrderCounterServices{

	private final IOrderCounterRepository countR;
	

	@Transactional (rollbackFor = Exception.class)
	@Override
	public String nextOrderNumber(Integer anno) throws Exception {
		log.debug("nextOrderNumber {}", anno);
		OrderCounter counter = countR.findById(anno)
				.orElseGet(() -> initValue(anno));
		
		Long next = counter.getCurrentValue() + 1;
		counter.setCurrentValue(next);
		countR.save(counter);		
		
		return anno.toString() + "-" + next.toString();
	}
	
	@Transactional (rollbackFor = Exception.class)
	@Override
	public String nextOrderProvisaryNumber(Integer anno) throws Exception {
		log.debug("nextOrderProvisaryNumber {}", anno);
		OrderCounter counter = countR.findById(anno)
				.orElseGet(() -> initValue(anno));
		
		Long prev = counter.getProvisaryValue() - 1;
		counter.setProvisaryValue(prev);
		countR.save(counter);
		
		
		return anno.toString() + "-" + prev.toString();
	}

	private OrderCounter initValue(Integer anno) {
		log.debug("initValue {}", anno);
		OrderCounter counter = new OrderCounter();
		counter.setId(anno);
		counter.setCurrentValue(0L);
		counter.setProvisaryValue(999999999999L);
		return counter;
		
	}
}
