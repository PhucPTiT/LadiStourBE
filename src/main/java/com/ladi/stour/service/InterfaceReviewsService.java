package com.ladi.stour.service;

import com.ladi.stour.dto.MessageResponse;
import com.ladi.stour.dto.ReviewsCreateRequest;
import com.ladi.stour.dto.ReviewsUpdateRequest;
import com.ladi.stour.entity.ReviewsEntity;

import java.util.List;

public interface InterfaceReviewsService {
    List<ReviewsEntity> getAll();
    ReviewsEntity create(ReviewsCreateRequest req);
    ReviewsEntity update(String id, ReviewsUpdateRequest req);
    ReviewsEntity approve(String id);
    ReviewsEntity reject(String id);
    MessageResponse delete(String id);
    ReviewsEntity getById(String id);
}
