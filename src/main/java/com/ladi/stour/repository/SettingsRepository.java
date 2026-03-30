package com.ladi.stour.repository;

import com.ladi.stour.entity.SettingsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SettingsRepository extends MongoRepository<SettingsEntity, String> {
}
