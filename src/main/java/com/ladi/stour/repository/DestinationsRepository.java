package com.ladi.stour.repository;

import com.ladi.stour.entity.DestinationsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DestinationsRepository extends MongoRepository<DestinationsEntity, String> {

    Optional<DestinationsEntity> findBySlugAndLocale(String slug, String locale);

    List<DestinationsEntity> findByTranslationGroupId(String translationGroupId);

    List<DestinationsEntity> findByLocaleAndIsFeatured(String locale, boolean isFeatured);

    List<DestinationsEntity> findByLocale(String locale);

    List<DestinationsEntity> findByLocationCountryAndLocale(String country, String locale);

    List<DestinationsEntity> findByLocationCityAndLocale(String city, String locale);
}
