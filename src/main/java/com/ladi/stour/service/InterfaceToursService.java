package com.ladi.stour.service;

import com.ladi.stour.dto.ToursCreateRequest;
import com.ladi.stour.dto.ToursUpdateRequest;
import com.ladi.stour.entity.ToursEntity;
import com.ladi.stour.enums.TourStatus;

import java.util.List;

public interface InterfaceToursService {
    ToursEntity createDefault(ToursCreateRequest req);
    ToursEntity createTranslation(String originTourId, ToursCreateRequest req);
    ToursEntity update(String id, ToursUpdateRequest req);
    ToursEntity publish(String id);
    ToursEntity archive(String id);
    void delete(String id);
    ToursEntity getBySlug(String slug, String locale);
    List<ToursEntity> getTranslations(String translationGroupId);
    List<ToursEntity> getAll(String locale);
    List<ToursEntity> getByStatus(String locale, TourStatus status);
    List<ToursEntity> getByDestination(String destinationId, String locale);
    List<ToursEntity> getPublishedByDestination(String destinationId, String locale);
    List<ToursEntity> getFeatured(String locale);
    List<ToursEntity> getFeaturedPublished(String locale);
    List<ToursEntity> getByTag(String tag, String locale);
    List<ToursEntity> getPublishedByTag(String tag, String locale);
}
