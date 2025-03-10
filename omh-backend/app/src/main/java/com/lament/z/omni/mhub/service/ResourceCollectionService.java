package com.lament.z.omni.mhub.service;

import com.lament.z.omni.mhub.model.ResourceCollection;
import com.lament.z.omni.mhub.model.dto.ResCollectionDTO;
import com.lament.z.omni.mhub.repository.ResourceCollectionRepository;
import com.lament.z.omni.mhub.repository.ResourceRelationRepository;
import com.lament.z.omni.mhub.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResourceCollectionService {

	private static final Logger log = LoggerFactory.getLogger(ResourceCollectionService.class);
	private final ResourceCollectionRepository resourceCollectionRepository;
	private final VideoRepository videoRepository;
	private final ResourceRelationRepository resourceRelationRepository;

	public ResourceCollectionService(ResourceCollectionRepository resourceCollectionRepository, VideoRepository videoRepository, ResourceRelationRepository resourceRelationRepository) {
		this.resourceCollectionRepository = resourceCollectionRepository;
		this.videoRepository = videoRepository;
		this.resourceRelationRepository = resourceRelationRepository;
	}

	/**
	 * 使“默认合集”全部可见
	 * <p>
	 * 默认合集: createBy == 'SYSTEM'
	 * */
	@Transactional
	public Mono<Void> visibleAll() {
		return resourceCollectionRepository.updateAllToVisible()
				.doOnNext(row ->  log.debug("rc effected row: {}",row))
				.then(Mono.empty());


	}

	public Flux<ResCollectionDTO> getAll() {
		return resourceCollectionRepository.findAll()
				.map(ResCollectionDTO::new);
	}

	public Mono<Void> updateOneRC(ResourceCollection rc) {
		return resourceCollectionRepository.save(rc)
				.doOnNext( row -> log.debug(" update rc:{} ,row: {}",rc,row) )
				.then(Mono.empty());
	}

	public Mono<Void> deleteRCById(Long rcID) {
		return resourceCollectionRepository.findById(rcID)
				.flatMap(resourceCollectionRepository::delete);
	}

	public Flux<ResCollectionDTO> getAllVisible() {
		ResourceCollection rcExample = new ResourceCollection();
		rcExample.setVisible(1);
		Example<ResourceCollection> example = Example.of(rcExample);
		return resourceCollectionRepository.findAll(example)
				.map(ResCollectionDTO::new);
	}

	public Mono<Void> truncateRC() {
		return resourceCollectionRepository.truncate()
				.then(videoRepository.truncate())
				.then(resourceRelationRepository.truncate());

	}
}
