package com.ladi.stour.repository;

import com.ladi.stour.entity.PostsEntity;
import com.ladi.stour.enums.ContentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostsRepository extends MongoRepository<PostsEntity, String> {

    Optional<PostsEntity> findBySlugAndLocale(String slug, String locale);
    boolean existsBySlug(String slug);
    boolean existsBySlugAndIdNot(String slug, String id);

    List<PostsEntity> findByTranslationGroupId(String translationGroupId);

    List<PostsEntity> findByCategoryIdAndLocale(String categoryId, String locale);
    List<PostsEntity> findByLocaleAndStatus(String locale, ContentStatus status);
}
