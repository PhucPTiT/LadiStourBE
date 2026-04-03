package com.ladi.stour.service;

import com.ladi.stour.dto.CategoriesCreateRequest;
import com.ladi.stour.dto.CategoriesUpdateRequest;
import com.ladi.stour.dto.MessageResponse;
import com.ladi.stour.entity.CategoriesEntity;

import java.util.List;

public interface InterfaceCategoriesService {
    CategoriesEntity createDefault(CategoriesCreateRequest req);
    CategoriesEntity update(String id, CategoriesUpdateRequest req);
    MessageResponse delete(String id);
    CategoriesEntity getBySlug(String slug, String locale);
    List<CategoriesEntity> getAll(String locale);
    List<CategoriesEntity> getAll();
    CategoriesEntity getById(String id);
}
