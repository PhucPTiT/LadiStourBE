package com.ladi.stour.controller;

import com.ladi.stour.dto.MessageResponse;
import com.ladi.stour.dto.ToursCreateRequest;
import com.ladi.stour.dto.ToursMultiLanguageRequest;
import com.ladi.stour.dto.ToursUpdateRequest;
import com.ladi.stour.entity.ToursEntity;
import com.ladi.stour.enums.TourStatus;
import com.ladi.stour.service.InterfaceToursService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tours")
@RequiredArgsConstructor
public class ToursController {
    private final InterfaceToursService toursService;

    @PostMapping
    public ToursEntity create(@RequestBody @Valid ToursCreateRequest req) {
        return toursService.createDefault(req);
    }

    @PostMapping("/multi-language")
    public MessageResponse createMultiLanguage(@RequestBody @Valid ToursMultiLanguageRequest req) {
        return toursService.createOrUpdateMultiLanguage(req);
    }

    @PostMapping("/{id}/translations")
    public ToursEntity createTranslation(
            @PathVariable String id,
            @RequestBody @Valid ToursCreateRequest req
    ) {
        return toursService.createTranslation(id, req);
    }

    @PutMapping("/{id}")
    public ToursEntity update(@PathVariable String id, @RequestBody @Valid ToursUpdateRequest req) {
        return toursService.update(id, req);
    }

    @PutMapping("/multi-language/{id}")
    public MessageResponse updateMultiLanguage(@PathVariable String id, @RequestBody @Valid ToursMultiLanguageRequest req) {
        return toursService.createOrUpdateMultiLanguage(id, req);
    }

    @PatchMapping("/{id}/publish")
    public ToursEntity publish(@PathVariable String id) {
        return toursService.publish(id);
    }

    @PatchMapping("/{id}/archive")
    public ToursEntity archive(@PathVariable String id) {
        return toursService.archive(id);
    }

    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable String id) {
        return toursService.delete(id);
    }

    @GetMapping("/slug/{slug}")
    public ToursEntity getBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "vi") String locale
    ) {
        return toursService.getBySlug(slug, locale);
    }

    @GetMapping("/translations/{translationGroupId}")
    public List<ToursEntity> getTranslations(@PathVariable String translationGroupId) {
        return toursService.getTranslations(translationGroupId);
    }

    @GetMapping
    public List<ToursEntity> getAll(@RequestParam(defaultValue = "vi") String locale) {
        return toursService.getAll(locale);
    }

    @GetMapping("/status/{status}")
    public List<ToursEntity> getByStatus(
            @PathVariable TourStatus status,
            @RequestParam(defaultValue = "vi") String locale
    ) {
        return toursService.getByStatus(locale, status);
    }

    @GetMapping("/destination/{destinationId}")
    public List<ToursEntity> getByDestination(
            @PathVariable String destinationId,
            @RequestParam(defaultValue = "vi") String locale
    ) {
        return toursService.getByDestination(destinationId, locale);
    }

    @GetMapping("/destination/{destinationId}/published")
    public List<ToursEntity> getPublishedByDestination(
            @PathVariable String destinationId,
            @RequestParam(defaultValue = "vi") String locale
    ) {
        return toursService.getPublishedByDestination(destinationId, locale);
    }

    @GetMapping("/featured")
    public List<ToursEntity> getFeatured(@RequestParam(defaultValue = "vi") String locale) {
        return toursService.getFeatured(locale);
    }

    @GetMapping("/featured/published")
    public List<ToursEntity> getFeaturedPublished(@RequestParam(defaultValue = "vi") String locale) {
        return toursService.getFeaturedPublished(locale);
    }

    @GetMapping("/tag/{tag}")
    public List<ToursEntity> getByTag(
            @PathVariable String tag,
            @RequestParam(defaultValue = "vi") String locale
    ) {
        return toursService.getByTag(tag, locale);
    }

    @GetMapping("/tag/{tag}/published")
    public List<ToursEntity> getPublishedByTag(
            @PathVariable String tag,
            @RequestParam(defaultValue = "vi") String locale
    ) {
        return toursService.getPublishedByTag(tag, locale);
    }
}
