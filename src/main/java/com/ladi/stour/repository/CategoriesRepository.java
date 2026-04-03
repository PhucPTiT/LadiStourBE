package com.ladi.stour.repository;

import com.ladi.stour.entity.CategoriesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepository extends MongoRepository<CategoriesEntity, String> {

    Optional<CategoriesEntity> findBySlugAndLocale(String slug, String locale);
    boolean existsBySlug(String slug);
    boolean existsBySlugAndIdNot(String slug, String id);

    List<CategoriesEntity> findByLocale(String locale);
}
