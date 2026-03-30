package com.ladi.stour.controller;

import com.ladi.stour.dto.CategoriesCreateRequest;
import com.ladi.stour.dto.CategoriesUpdateRequest;
import com.ladi.stour.entity.CategoriesEntity;
import com.ladi.stour.service.InterfaceCategoriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoriesController {
    private final InterfaceCategoriesService categoriesService;

    @PostMapping
    public CategoriesEntity create(@RequestBody @Valid CategoriesCreateRequest req) {
        return categoriesService.createDefault(req);
    }

    @PostMapping("/{id}/translations")
    public CategoriesEntity createTranslation(
            @PathVariable String id,
            @RequestBody @Valid CategoriesCreateRequest req
    ) {
        return categoriesService.createTranslation(id, req);
    }

    @PutMapping("/{id}")
    public CategoriesEntity update(@PathVariable String id, @RequestBody @Valid CategoriesUpdateRequest req) {
        return categoriesService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        categoriesService.delete(id);
    }

    @GetMapping("/{id}")
    public CategoriesEntity getById(@PathVariable String id) {
        return categoriesService.getById(id);
    }

    @GetMapping("/slug/{slug}")
    public CategoriesEntity getBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "vi") String locale
    ) {
        return categoriesService.getBySlug(slug, locale);
    }

    @GetMapping("/translations/{translationGroupId}")
    public List<CategoriesEntity> getTranslations(@PathVariable String translationGroupId) {
        return categoriesService.getTranslations(translationGroupId);
    }

    @GetMapping
    public List<CategoriesEntity> getAll(@RequestParam(defaultValue = "vi") String locale) {
        return categoriesService.getAll(locale);
    }
}
