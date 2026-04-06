package com.ladi.stour.repository;

import com.ladi.stour.entity.ServiceHighlightsSectionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ServiceHighlightsSectionRepository extends MongoRepository<ServiceHighlightsSectionEntity, String> {
    Optional<ServiceHighlightsSectionEntity> findFirstByOrderByCreatedAtAsc();
}
