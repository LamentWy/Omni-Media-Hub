package com.lament.z.omni.mhub.repository;

import com.lament.z.omni.mhub.model.ResourceCollection;
import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ResourceCollectionRepository extends R2dbcRepository<ResourceCollection,Long> {
	@Transactional(isolation = Isolation.READ_COMMITTED)
	Mono<ResourceCollection> findOneByName(String cName);

	@Modifying
	@Query("update Resource_Collection set visible = 1,last_modified_by ='SYSTEM' where created_by = 'SYSTEM'")
	Mono<Integer> updateAllToVisible();
}
