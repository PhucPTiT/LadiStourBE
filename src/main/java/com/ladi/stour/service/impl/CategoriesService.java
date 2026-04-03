package com.ladi.stour.service.impl;

import com.ladi.stour.common.SlugGenerator;
import com.ladi.stour.dto.CategoriesCreateRequest;
import com.ladi.stour.dto.CategoriesUpdateRequest;
import com.ladi.stour.dto.MessageResponse;
import com.ladi.stour.entity.CategoriesEntity;
import com.ladi.stour.repository.CategoriesRepository;
import com.ladi.stour.service.InterfaceCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesService implements InterfaceCategoriesService {
    private final CategoriesRepository categoriesRepository;

    @Override
    public CategoriesEntity createDefault(CategoriesCreateRequest req) {
        CategoriesEntity category = CategoriesEntity.builder()
                .locale(req.getLocale())
                .name(req.getName())
                .slug(resolveSlugForCreate(req.getSlug(), req.getName()))
                .build();

        return categoriesRepository.save(category);
    }

    @Override
    public CategoriesEntity update(String id, CategoriesUpdateRequest req) {
        CategoriesEntity category = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (req.getName() != null) category.setName(req.getName());
        if (req.getSlug() != null && !req.getSlug().isBlank()) {
            category.setSlug(resolveSlugForUpdate(req.getSlug(), id));
        }

        return categoriesRepository.save(category);
    }

    @Override
    public MessageResponse delete(String id) {
        categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoriesRepository.deleteById(id);
        return new MessageResponse("Xoa category thanh cong");
    }

    @Override
    public CategoriesEntity getBySlug(String slug, String locale) {
        return categoriesRepository.findBySlugAndLocale(slug, locale)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<CategoriesEntity> getAll(String locale) {
        return categoriesRepository.findByLocale(locale);
    }

    @Override
    public List<CategoriesEntity> getAll() {
        return categoriesRepository.findAll();
    }

    @Override
    public CategoriesEntity getById(String id) {
        return categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    private String resolveSlugForCreate(String requestedSlug, String fallbackName) {
        String baseSlug = buildBaseSlug(requestedSlug, fallbackName);
        String slug = baseSlug;
        int counter = 1;

        while (categoriesRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }

    private String resolveSlugForUpdate(String requestedSlug, String id) {
        String baseSlug = buildBaseSlug(requestedSlug, null);
        String slug = baseSlug;
        int counter = 1;

        while (categoriesRepository.existsBySlugAndIdNot(slug, id)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }

    private String buildBaseSlug(String requestedSlug, String fallbackValue) {
        String source = requestedSlug != null && !requestedSlug.isBlank() ? requestedSlug : fallbackValue;
        String slug = SlugGenerator.generateSlug(source);
        if (slug.isBlank()) {
            throw new RuntimeException("Unable to generate slug");
        }
        return slug;
    }
}
