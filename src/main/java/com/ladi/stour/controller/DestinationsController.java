package com.ladi.stour.controller;

import com.ladi.stour.dto.DestinationsCreateRequest;
import com.ladi.stour.dto.DestinationsUpdateRequest;
import com.ladi.stour.entity.DestinationsEntity;
import com.ladi.stour.service.InterfaceDestinationsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
public class DestinationsController {
    private final InterfaceDestinationsService destinationsService;

    @PostMapping
    public DestinationsEntity create(@RequestBody @Valid DestinationsCreateRequest req) {
        return destinationsService.createDefault(req);
    }

    @PostMapping("/{id}/translations")
    public DestinationsEntity createTranslation(
            @PathVariable String id,
            @RequestBody @Valid DestinationsCreateRequest req
    ) {
        return destinationsService.createTranslation(id, req);
    }

    @PutMapping("/{id}")
    public DestinationsEntity update(@PathVariable String id, @RequestBody @Valid DestinationsUpdateRequest req) {
        return destinationsService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        destinationsService.delete(id);
    }

    @GetMapping("/slug/{slug}")
    public DestinationsEntity getBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "vi") String locale
    ) {
        return destinationsService.getBySlug(slug, locale);
    }

    @GetMapping("/translations/{translationGroupId}")
    public List<DestinationsEntity> getTranslations(@PathVariable String translationGroupId) {
        return destinationsService.getTranslations(translationGroupId);
    }

    @GetMapping
    public List<DestinationsEntity> getAll(@RequestParam(defaultValue = "vi") String locale) {
        return destinationsService.getAll(locale);
    }

    @GetMapping("/featured")
    public List<DestinationsEntity> getFeatured(@RequestParam(defaultValue = "vi") String locale) {
        return destinationsService.getFeatured(locale);
    }

    @GetMapping("/country/{country}")
    public List<DestinationsEntity> getByCountry(
            @PathVariable String country,
            @RequestParam(defaultValue = "vi") String locale
    ) {
        return destinationsService.getByCountry(country, locale);
    }

    @GetMapping("/city/{city}")
    public List<DestinationsEntity> getByCity(
            @PathVariable String city,
            @RequestParam(defaultValue = "vi") String locale
    ) {
        return destinationsService.getByCity(city, locale);
    }
}
