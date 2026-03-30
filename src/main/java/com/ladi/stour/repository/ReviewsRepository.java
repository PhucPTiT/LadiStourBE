package com.ladi.stour.repository;

import com.ladi.stour.entity.ReviewsEntity;
import com.ladi.stour.enums.ReviewType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewsRepository extends MongoRepository<ReviewsEntity, String> {

    List<ReviewsEntity> findByTourIdAndLocale(String tourId, String locale);

    List<ReviewsEntity> findByTourIdAndLocaleAndIsApproved(String tourId, String locale, Boolean isApproved);

    List<ReviewsEntity> findByCompanyIdAndLocale(String companyId, String locale);

    List<ReviewsEntity> findByCompanyIdAndLocaleAndIsApproved(String companyId, String locale, Boolean isApproved);

    List<ReviewsEntity> findByTypeAndIsApproved(ReviewType type, Boolean isApproved);
}
