package com.lament.z.omni.mhub.web.controller;

import com.lament.z.omni.mhub.model.dto.ResCollectionDTO;
import com.lament.z.omni.mhub.model.dto.ResDTO;
import com.lament.z.omni.mhub.service.ResService;
import com.lament.z.omni.mhub.service.ResourceCollectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/z/res")
public class ResController {
	private static final Logger log = LoggerFactory.getLogger(ResController.class);

	private final ResourceCollectionService resourceCollectionService;
	private final ResService resService;

	public ResController(ResourceCollectionService resourceCollectionService, ResService resService) {
		this.resourceCollectionService = resourceCollectionService;
		this.resService = resService;
	}

	@GetMapping("/rc")
	public Flux<ResCollectionDTO> getAllVisibleRC(){
		return resourceCollectionService.getAllVisible();
	}

	@GetMapping("/rc/{id}")
	public Flux<ResDTO> getResourceListByRcID(@PathVariable("id") Integer rcId){
		return resService.getResListByRcId(rcId);
	}

}
