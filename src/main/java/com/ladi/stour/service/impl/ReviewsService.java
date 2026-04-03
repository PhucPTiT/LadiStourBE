package com.ladi.stour.service.impl;

import com.ladi.stour.dto.MessageResponse;
import com.ladi.stour.dto.ReviewsCreateRequest;
import com.ladi.stour.dto.ReviewsUpdateRequest;
import com.ladi.stour.entity.ReviewsEntity;
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
    public List<ReviewsEntity> getAll() {
        return reviewsRepository.findAll();
    }

    @Override
    public ReviewsEntity create(ReviewsCreateRequest req) {
        ReviewsEntity review = ReviewsEntity.builder()
                .locale(req.getLocale() != null ? req.getLocale() : "vi")
                .rating(req.getRating())
                .comment(req.getComment())
                .authorName(req.getAuthorName())
                .authorAvatar(req.getAuthorAvatar())
                .isApproved(true)
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
    public MessageResponse delete(String id) {
        reviewsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        reviewsRepository.deleteById(id);
        return new MessageResponse("Xóa review thành công");
    }

    @Override
    public ReviewsEntity getById(String id) {
        return reviewsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }
}
