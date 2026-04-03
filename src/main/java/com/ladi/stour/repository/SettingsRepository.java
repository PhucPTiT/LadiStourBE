package com.ladi.stour.repository;

import com.ladi.stour.entity.SettingsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SettingsRepository extends MongoRepository<SettingsEntity, String> {
    Optional<SettingsEntity> findFirstByOrderByCreatedAtAsc();
}
