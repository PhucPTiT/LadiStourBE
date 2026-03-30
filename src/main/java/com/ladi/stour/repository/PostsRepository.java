package com.ladi.stour.repository;

import com.ladi.stour.entity.PostsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostsRepository extends MongoRepository<PostsEntity, String> {

    Optional<PostsEntity> findBySlugAndLocale(String slug, String locale);

    List<PostsEntity> findByTranslationGroupId(String translationGroupId);

    List<PostsEntity> findByCategoryIdAndLocale(String categoryId, String locale);
}
