package com.ladi.stour.service.impl;

import com.ladi.stour.dto.CategoriesCreateRequest;
import com.ladi.stour.dto.CategoriesUpdateRequest;
import com.ladi.stour.entity.CategoriesEntity;
import com.ladi.stour.repository.CategoriesRepository;
import com.ladi.stour.service.InterfaceCategoriesService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesService implements InterfaceCategoriesService {
    private final CategoriesRepository categoriesRepository;

    @Override
    public CategoriesEntity createDefault(CategoriesCreateRequest req) {
        String groupId = new ObjectId().toString();

        CategoriesEntity category = CategoriesEntity.builder()
                .locale(req.getLocale())
                .translationGroupId(groupId)
                .originId(null)
                .isDefaultLocale(true)
                .name(req.getName())
                .slug(req.getSlug())
                .build();

        return categoriesRepository.save(category);
    }

    @Override
    public CategoriesEntity createTranslation(String originCategoryId, CategoriesCreateRequest req) {
        CategoriesEntity origin = categoriesRepository.findById(originCategoryId)
                .orElseThrow(() -> new RuntimeException("Origin category not found"));

        CategoriesEntity category = CategoriesEntity.builder()
                .locale(req.getLocale())
                .translationGroupId(origin.getTranslationGroupId())
                .originId(origin.getId())
                .isDefaultLocale(false)
                .name(req.getName())
                .slug(req.getSlug())
                .build();

        return categoriesRepository.save(category);
    }

    @Override
    public CategoriesEntity update(String id, CategoriesUpdateRequest req) {
        CategoriesEntity category = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (req.getName() != null) category.setName(req.getName());
        if (req.getSlug() != null) category.setSlug(req.getSlug());

        return categoriesRepository.save(category);
    }

    @Override
    public void delete(String id) {
        categoriesRepository.deleteById(id);
    }

    @Override
    public CategoriesEntity getBySlug(String slug, String locale) {
        return categoriesRepository.findBySlugAndLocale(slug, locale)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<CategoriesEntity> getTranslations(String translationGroupId) {
        return categoriesRepository.findByTranslationGroupId(translationGroupId);
    }

    @Override
    public List<CategoriesEntity> getAll(String locale) {
        return categoriesRepository.findByLocale(locale);
    }

    @Override
    public CategoriesEntity getById(String id) {
        return categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
