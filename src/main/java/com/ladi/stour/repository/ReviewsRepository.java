package com.ladi.stour.repository;

import com.ladi.stour.entity.ReviewsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewsRepository extends MongoRepository<ReviewsEntity, String> {
}
