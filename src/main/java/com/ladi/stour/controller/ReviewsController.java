package com.ladi.stour.controller;

import com.ladi.stour.dto.ReviewsCreateRequest;
import com.ladi.stour.dto.ReviewsUpdateRequest;
import com.ladi.stour.entity.ReviewsEntity;
import com.ladi.stour.enums.ReviewType;
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
    public void delete(@PathVariable String id) {
        reviewsService.delete(id);
    }

    @GetMapping("/{id}")
    public ReviewsEntity getById(@PathVariable String id) {
        return reviewsService.getById(id);
    }

    @GetMapping("/tour/{tourId}")
    public List<ReviewsEntity> getByTourId(
            @PathVariable String tourId,
            @RequestParam(defaultValue = "vi") String locale
    ) {
        return reviewsService.getByTourId(tourId, locale);
    }

    @GetMapping("/tour/{tourId}/approved")
    public List<ReviewsEntity> getApprovedByTourId(
            @PathVariable String tourId,
            @RequestParam(defaultValue = "vi") String locale
    ) {
        return reviewsService.getApprovedByTourId(tourId, locale);
    }

    @GetMapping("/company/{companyId}")
    public List<ReviewsEntity> getByCompanyId(
            @PathVariable String companyId,
            @RequestParam(defaultValue = "vi") String locale
    ) {
        return reviewsService.getByCompanyId(companyId, locale);
    }

    @GetMapping("/company/{companyId}/approved")
    public List<ReviewsEntity> getApprovedByCompanyId(
            @PathVariable String companyId,
            @RequestParam(defaultValue = "vi") String locale
    ) {
        return reviewsService.getApprovedByCompanyId(companyId, locale);
    }

    @GetMapping("/type/{type}/approved")
    public List<ReviewsEntity> getApprovedByType(@PathVariable ReviewType type) {
        return reviewsService.getApprovedByType(type);
    }
}
