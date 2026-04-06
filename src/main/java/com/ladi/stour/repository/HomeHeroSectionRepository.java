package com.ladi.stour.repository;

import com.ladi.stour.entity.HomeHeroSectionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface HomeHeroSectionRepository extends MongoRepository<HomeHeroSectionEntity, String> {
    Optional<HomeHeroSectionEntity> findFirstByOrderByCreatedAtAsc();
}
