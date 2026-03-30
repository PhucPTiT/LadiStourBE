package com.ladi.stour.service;

import com.ladi.stour.dto.ReviewsCreateRequest;
import com.ladi.stour.dto.ReviewsUpdateRequest;
import com.ladi.stour.entity.ReviewsEntity;
import com.ladi.stour.enums.ReviewType;

import java.util.List;

public interface InterfaceReviewsService {
    ReviewsEntity create(ReviewsCreateRequest req);
    ReviewsEntity update(String id, ReviewsUpdateRequest req);
    ReviewsEntity approve(String id);
    ReviewsEntity reject(String id);
    void delete(String id);
    ReviewsEntity getById(String id);
    List<ReviewsEntity> getByTourId(String tourId, String locale);
    List<ReviewsEntity> getApprovedByTourId(String tourId, String locale);
    List<ReviewsEntity> getByCompanyId(String companyId, String locale);
    List<ReviewsEntity> getApprovedByCompanyId(String companyId, String locale);
    List<ReviewsEntity> getApprovedByType(ReviewType type);
}
