package com.lament.z.omni.mhub.repository;

import com.lament.z.omni.mhub.model.User;
import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends R2dbcRepository<User,Integer> {

	// R2dbcRepository 可以直接解析方法名并转为 sql。
	// 比如 findByName -> select * from user where login_name = :name 。
	// 也可以随便起个好认的方法名，通过 @Query 手写 sql。
	// 详见 https://docs.spring.io/spring-data/relational/reference/r2dbc/query-methods.html


	Mono<User> findByName(String name);

	@Query("select * from user where activated = 1 and login_name = :username")
	Mono<User> findOneWithActivatedByName(String username);

	@Modifying
	@Query("update user set password_hash = :#{#user.password}, "
			+ "last_modified_date = :#{#user.lastModifiedDate},"
			+ " last_modified_by = :#{#user.lastModifiedBy}  where id = :#{#user.id}")
	Mono<Integer> changePasswordByUserId(@Param("user") User user);
}
