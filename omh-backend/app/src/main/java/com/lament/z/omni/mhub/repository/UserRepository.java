package com.lament.z.omni.mhub.repository;

import com.lament.z.omni.mhub.model.User;
import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends R2dbcRepository<User,Integer> {


	Mono<User> findByName(String name);

	@Query("select * from user where activated = 1 and login_name = :username")
	Mono<User> findOneWithActivatedByName(String username);
}
