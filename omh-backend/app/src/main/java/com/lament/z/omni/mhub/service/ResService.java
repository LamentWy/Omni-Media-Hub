package com.lament.z.omni.mhub.service;

import com.lament.z.omni.mhub.model.dto.ResDTO;
import com.lament.z.omni.mhub.repository.ResourceRelationRepository;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Service;

@Service
public class ResService {


	private final ResourceRelationRepository resourceRelationRepository;

	public ResService(ResourceRelationRepository resourceRelationRepository) {
		this.resourceRelationRepository = resourceRelationRepository;
	}

	public Flux<ResDTO> getResListByRcId(Integer rcId) {
		return resourceRelationRepository.getResListByRcId(rcId);
	}
}
