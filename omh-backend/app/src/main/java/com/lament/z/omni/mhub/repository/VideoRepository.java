package com.lament.z.omni.mhub.repository;

import com.lament.z.omni.mhub.model.Video;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends R2dbcRepository<Video, Long> {


}
