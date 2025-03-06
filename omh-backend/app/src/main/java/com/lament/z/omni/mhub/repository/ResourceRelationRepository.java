package com.lament.z.omni.mhub.repository;

import com.lament.z.omni.mhub.model.ResourceRelation;
import com.lament.z.omni.mhub.model.dto.ResDTO;
import reactor.core.publisher.Flux;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRelationRepository extends R2dbcRepository<ResourceRelation, Long> {

	@Query("select\n"
			+ "\n"
			+ "\tv.id as id, v.title as title, v.file_name as file_name, v.file_path as file_path,\n"
			+ "  v.desc as description, v.cover as cover, rr.c_id as c_id, rc.name as c_name, rc.description as c_desc \n"
			+ "  \n"
			+ "\n"
			+ "from Resource_Relation as rr \n"
			+ "join Resource_Collection as rc on  rr.c_id = rc.id\n"
			+ "\n"
			+ "join Video as v on rr.v_id = v.id\n"
			+ "\n"
			+ "where \n"
			+ "rr.c_id = :rcId")
	Flux<ResDTO> getResListByRcId(Integer rcId);
}
