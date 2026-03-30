package com.ladi.stour.service.impl;

import com.ladi.stour.dto.DestinationsCreateRequest;
import com.ladi.stour.dto.DestinationsUpdateRequest;
import com.ladi.stour.embedded.SEOMeta;
import com.ladi.stour.entity.DestinationsEntity;
import com.ladi.stour.repository.DestinationsRepository;
import com.ladi.stour.service.InterfaceDestinationsService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationsService implements InterfaceDestinationsService {
    private final DestinationsRepository destinationsRepository;

    @Override
    public DestinationsEntity createDefault(DestinationsCreateRequest req) {
        String groupId = new ObjectId().toString();

        DestinationsEntity destination = DestinationsEntity.builder()
                .locale(req.getLocale())
                .translationGroupId(groupId)
                .originId(null)
                .isDefaultLocale(true)
                .name(req.getName())
                .slug(req.getSlug())
                .thumbnail(req.getThumbnail())
                .banner(req.getBanner())
                .shortDescription(req.getShortDescription())
                .description(req.getDescription())
                .location(req.getLocation())
                .isFeatured(req.isFeatured())
                .seo(mapSeo(req))
                .build();

        return destinationsRepository.save(destination);
    }

    @Override
    public DestinationsEntity createTranslation(String originDestinationId, DestinationsCreateRequest req) {
        DestinationsEntity origin = destinationsRepository.findById(originDestinationId)
                .orElseThrow(() -> new RuntimeException("Origin destination not found"));

        DestinationsEntity destination = DestinationsEntity.builder()
                .locale(req.getLocale())
                .translationGroupId(origin.getTranslationGroupId())
                .originId(origin.getId())
                .isDefaultLocale(false)
                .name(req.getName())
                .slug(req.getSlug())
                .thumbnail(req.getThumbnail())
                .banner(req.getBanner())
                .shortDescription(req.getShortDescription())
                .description(req.getDescription())
                .location(req.getLocation())
                .isFeatured(req.isFeatured())
                .seo(mapSeo(req))
                .build();

        return destinationsRepository.save(destination);
    }

    @Override
    public DestinationsEntity update(String id, DestinationsUpdateRequest req) {
        DestinationsEntity destination = destinationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        if (req.getName() != null) destination.setName(req.getName());
        if (req.getSlug() != null) destination.setSlug(req.getSlug());
        if (req.getThumbnail() != null) destination.setThumbnail(req.getThumbnail());
        if (req.getBanner() != null) destination.setBanner(req.getBanner());
        if (req.getShortDescription() != null) destination.setShortDescription(req.getShortDescription());
        if (req.getDescription() != null) destination.setDescription(req.getDescription());
        if (req.getLocation() != null) destination.setLocation(req.getLocation());
        if (req.getIsFeatured() != null) destination.setFeatured(req.getIsFeatured());

        if (req.getSeo() != null) {
            SEOMeta seo = new SEOMeta();
            seo.setTitle(req.getSeo().getTitle());
            seo.setDescription(req.getSeo().getDescription());
            seo.setKeywords(req.getSeo().getKeywords());
            destination.setSeo(seo);
        }

        return destinationsRepository.save(destination);
    }

    @Override
    public void delete(String id) {
        destinationsRepository.deleteById(id);
    }

    @Override
    public DestinationsEntity getBySlug(String slug, String locale) {
        return destinationsRepository.findBySlugAndLocale(slug, locale)
                .orElseThrow(() -> new RuntimeException("Destination not found"));
    }

    @Override
    public List<DestinationsEntity> getTranslations(String translationGroupId) {
        return destinationsRepository.findByTranslationGroupId(translationGroupId);
    }

    @Override
    public List<DestinationsEntity> getAll(String locale) {
        return destinationsRepository.findByLocale(locale);
    }

    @Override
    public List<DestinationsEntity> getFeatured(String locale) {
        return destinationsRepository.findByLocaleAndIsFeatured(locale, true);
    }

    @Override
    public List<DestinationsEntity> getByCountry(String country, String locale) {
        return destinationsRepository.findByLocationCountryAndLocale(country, locale);
    }

    @Override
    public List<DestinationsEntity> getByCity(String city, String locale) {
        return destinationsRepository.findByLocationCityAndLocale(city, locale);
    }

    private SEOMeta mapSeo(DestinationsCreateRequest req) {
        if (req.getSeo() == null) return null;

        SEOMeta seo = new SEOMeta();
        seo.setTitle(req.getSeo().getTitle());
        seo.setDescription(req.getSeo().getDescription());
        seo.setKeywords(req.getSeo().getKeywords());
        return seo;
    }
}
