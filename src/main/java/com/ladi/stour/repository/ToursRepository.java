package com.ladi.stour.repository;

import com.ladi.stour.entity.ToursEntity;
import com.ladi.stour.enums.TourStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ToursRepository extends MongoRepository<ToursEntity, String> {

    Optional<ToursEntity> findBySlugAndLocale(String slug, String locale);
    boolean existsBySlug(String slug);
    boolean existsBySlugAndIdNot(String slug, String id);

    Optional<ToursEntity> findByTranslationGroupIdAndLocale(String translationGroupId, String locale);

    List<ToursEntity> findByTranslationGroupId(String translationGroupId);

    List<ToursEntity> findByLocaleAndStatus(String locale, TourStatus status);

    List<ToursEntity> findByLocale(String locale);

    List<ToursEntity> findByDestinationIdAndLocale(String destinationId, String locale);

    List<ToursEntity> findByDestinationIdAndLocaleAndStatus(String destinationId, String locale, TourStatus status);

    List<ToursEntity> findByLocaleAndIsFeatured(String locale, boolean isFeatured);

    List<ToursEntity> findByLocaleAndStatusAndIsFeatured(String locale, TourStatus status, boolean isFeatured);

    List<ToursEntity> findByTagsContainingAndLocale(String tag, String locale);

    List<ToursEntity> findByTagsContainingAndLocaleAndStatus(String tag, String locale, TourStatus status);
}
