package com.ladi.stour.service.impl;

import com.ladi.stour.dto.ReviewsCreateRequest;
import com.ladi.stour.dto.ReviewsUpdateRequest;
import com.ladi.stour.entity.ReviewsEntity;
import com.ladi.stour.enums.ReviewType;
import com.ladi.stour.repository.ReviewsRepository;
import com.ladi.stour.service.InterfaceReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewsService implements InterfaceReviewsService {
    private final ReviewsRepository reviewsRepository;

    @Override
    public ReviewsEntity create(ReviewsCreateRequest req) {
        ReviewsEntity review = ReviewsEntity.builder()
                .type(req.getType())
                .tourId(req.getTourId())
                .companyId(req.getCompanyId())
                .locale(req.getLocale() != null ? req.getLocale() : "vi")
                .rating(req.getRating())
                .comment(req.getComment())
                .authorName(req.getAuthorName())
                .authorAvatar(req.getAuthorAvatar())
                .isApproved(false)
                .build();

        return reviewsRepository.save(review);
    }

    @Override
    public ReviewsEntity update(String id, ReviewsUpdateRequest req) {
        ReviewsEntity review = reviewsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (req.getRating() != null) review.setRating(req.getRating());
        if (req.getComment() != null) review.setComment(req.getComment());
        if (req.getAuthorName() != null) review.setAuthorName(req.getAuthorName());
        if (req.getAuthorAvatar() != null) review.setAuthorAvatar(req.getAuthorAvatar());

        return reviewsRepository.save(review);
    }

    @Override
    public ReviewsEntity approve(String id) {
        ReviewsEntity review = reviewsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setIsApproved(true);
        return reviewsRepository.save(review);
    }

    @Override
    public ReviewsEntity reject(String id) {
        ReviewsEntity review = reviewsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setIsApproved(false);
        return reviewsRepository.save(review);
    }

    @Override
    public void delete(String id) {
        reviewsRepository.deleteById(id);
    }

    @Override
    public ReviewsEntity getById(String id) {
        return reviewsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    @Override
    public List<ReviewsEntity> getByTourId(String tourId, String locale) {
        return reviewsRepository.findByTourIdAndLocale(tourId, locale);
    }

    @Override
    public List<ReviewsEntity> getApprovedByTourId(String tourId, String locale) {
        return reviewsRepository.findByTourIdAndLocaleAndIsApproved(tourId, locale, true);
    }

    @Override
    public List<ReviewsEntity> getByCompanyId(String companyId, String locale) {
        return reviewsRepository.findByCompanyIdAndLocale(companyId, locale);
    }

    @Override
    public List<ReviewsEntity> getApprovedByCompanyId(String companyId, String locale) {
        return reviewsRepository.findByCompanyIdAndLocaleAndIsApproved(companyId, locale, true);
    }

    @Override
    public List<ReviewsEntity> getApprovedByType(ReviewType type) {
        return reviewsRepository.findByTypeAndIsApproved(type, true);
    }
}
