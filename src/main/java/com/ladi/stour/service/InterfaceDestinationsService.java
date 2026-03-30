package com.ladi.stour.service;

import com.ladi.stour.dto.DestinationsCreateRequest;
import com.ladi.stour.dto.DestinationsUpdateRequest;
import com.ladi.stour.entity.DestinationsEntity;

import java.util.List;

public interface InterfaceDestinationsService {
    DestinationsEntity createDefault(DestinationsCreateRequest req);
    DestinationsEntity createTranslation(String originDestinationId, DestinationsCreateRequest req);
    DestinationsEntity update(String id, DestinationsUpdateRequest req);
    void delete(String id);
    DestinationsEntity getBySlug(String slug, String locale);
    List<DestinationsEntity> getTranslations(String translationGroupId);
    List<DestinationsEntity> getAll(String locale);
    List<DestinationsEntity> getFeatured(String locale);
    List<DestinationsEntity> getByCountry(String country, String locale);
    List<DestinationsEntity> getByCity(String city, String locale);
}
