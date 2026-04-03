package com.ladi.stour.controller;

import com.ladi.stour.dto.MessageResponse;
import com.ladi.stour.dto.ReviewsCreateRequest;
import com.ladi.stour.dto.ReviewsUpdateRequest;
import com.ladi.stour.entity.ReviewsEntity;
import com.ladi.stour.service.InterfaceReviewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewsController {
    private final InterfaceReviewsService reviewsService;

    @GetMapping
    public List<ReviewsEntity> getAll() {
        return reviewsService.getAll();
    }

    @PostMapping
    public ReviewsEntity create(@RequestBody @Valid ReviewsCreateRequest req) {
        return reviewsService.create(req);
    }

    @PutMapping("/{id}")
    public ReviewsEntity update(@PathVariable String id, @RequestBody @Valid ReviewsUpdateRequest req) {
        return reviewsService.update(id, req);
    }

    @PatchMapping("/{id}/approve")
    public ReviewsEntity approve(@PathVariable String id) {
        return reviewsService.approve(id);
    }

    @PatchMapping("/{id}/reject")
    public ReviewsEntity reject(@PathVariable String id) {
        return reviewsService.reject(id);
    }

    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable String id) {
        return reviewsService.delete(id);
    }

    @GetMapping("/{id}")
    public ReviewsEntity getById(@PathVariable String id) {
        return reviewsService.getById(id);
    }
}
