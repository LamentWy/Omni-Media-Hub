package com.lament.z.omni.mhub.web.controller.admin;

import com.lament.z.omni.mhub.model.ResourceCollection;
import com.lament.z.omni.mhub.model.dto.ResCollectionDTO;
import com.lament.z.omni.mhub.model.vm.ResRootVM;
import com.lament.z.omni.mhub.repository.ResourceRelationRepository;
import com.lament.z.omni.mhub.scan.OmniResourceScanner;
import com.lament.z.omni.mhub.scan.ScanResult;
import com.lament.z.omni.mhub.service.ResourceCollectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/z/admin/res")
public class AdminResCollectionController {

	private static final Logger log = LoggerFactory.getLogger(AdminResCollectionController.class);
	private final OmniResourceScanner omniResourceScanner;
	private final ResourceCollectionService resourceCollectionService;

	public AdminResCollectionController(OmniResourceScanner omniResourceScanner, ResourceRelationRepository resourceRelationRepository, ResourceCollectionService resourceCollectionService) {
		this.omniResourceScanner = omniResourceScanner;
		this.resourceCollectionService = resourceCollectionService;
	}


	@PostMapping("/scan")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public Mono<ScanResult> resourceScan(@RequestBody ResRootVM resRootVM){

		return omniResourceScanner.scan(resRootVM)
				.flatMap(omniResourceScanner::process);
	}


	/**
	 * 所有默认合集可见。
	 * */
	@PutMapping("/visible")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public Mono<Void> visibleAll(){

		return resourceCollectionService.visibleAll();
	}

	/**
	 * 获取所有合集
	 * */
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public Flux<ResCollectionDTO> getAllResCollections(){
		return resourceCollectionService.getAll();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public Mono<Void> updateRC(@RequestBody ResCollectionDTO rcDTO){
		ResourceCollection rc = new ResourceCollection();
		rc.setId(rcDTO.getId());
		rc.setName(rcDTO.getName());
		rc.setDescription(rcDTO.getDescription());
		rc.setVisible(rcDTO.getVisible());
		return resourceCollectionService.updateOneRC(rc);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<ResponseEntity<Void>> deleteRC(@PathVariable("id") Integer rcID) {
		return resourceCollectionService.deleteRCById(Long.valueOf(rcID))
				.then(Mono.just(ResponseEntity.noContent().build()));

	}

}
