package com.lament.z.omni.mhub.repository;

import com.lament.z.omni.mhub.model.UserRole;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends R2dbcRepository<UserRole,Integer> {
	Flux<UserRole> findByUserId(Integer userId);

	@Modifying
	@Query("delete from user_role where user_id = :userId ")
	Mono<Void> deleteUserId(Integer userId);
}
