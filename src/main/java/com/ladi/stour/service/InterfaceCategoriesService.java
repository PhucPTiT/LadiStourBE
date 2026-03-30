package com.ladi.stour.service;

import com.ladi.stour.dto.CategoriesCreateRequest;
import com.ladi.stour.dto.CategoriesUpdateRequest;
import com.ladi.stour.entity.CategoriesEntity;

import java.util.List;

public interface InterfaceCategoriesService {
    CategoriesEntity createDefault(CategoriesCreateRequest req);
    CategoriesEntity createTranslation(String originCategoryId, CategoriesCreateRequest req);
    CategoriesEntity update(String id, CategoriesUpdateRequest req);
    void delete(String id);
    CategoriesEntity getBySlug(String slug, String locale);
    List<CategoriesEntity> getTranslations(String translationGroupId);
    List<CategoriesEntity> getAll(String locale);
    CategoriesEntity getById(String id);
}
