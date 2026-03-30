package com.ladi.stour.service.impl;

import com.ladi.stour.dto.ToursCreateRequest;
import com.ladi.stour.dto.ToursUpdateRequest;
import com.ladi.stour.embedded.SEOMeta;
import com.ladi.stour.entity.ToursEntity;
import com.ladi.stour.enums.TourStatus;
import com.ladi.stour.repository.ToursRepository;
import com.ladi.stour.service.InterfaceToursService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToursService implements InterfaceToursService {
    private final ToursRepository toursRepository;

    @Override
    public ToursEntity createDefault(ToursCreateRequest req) {
        String groupId = new ObjectId().toString();

        ToursEntity tour = ToursEntity.builder()
                .locale(req.getLocale())
                .translationGroupId(groupId)
                .originId(null)
                .isDefaultLocale(true)
                .title(req.getTitle())
                .slug(req.getSlug())
                .destinationId(req.getDestinationId())
                .images(req.getImages())
                .durationDays(req.getDurationDays())
                .maxPeople(req.getMaxPeople())
                .price(req.getPrice())
                .salePrice(req.getSalePrice())
                .currency(req.getCurrency() != null ? req.getCurrency() : "VND")
                .description(req.getDescription())
                .itinerary(req.getItinerary())
                .tags(req.getTags())
                .isFeatured(req.isFeatured())
                .status(TourStatus.draft)
                .seo(mapSeo(req))
                .build();

        return toursRepository.save(tour);
    }

    @Override
    public ToursEntity createTranslation(String originTourId, ToursCreateRequest req) {
        ToursEntity origin = toursRepository.findById(originTourId)
                .orElseThrow(() -> new RuntimeException("Origin tour not found"));

        ToursEntity tour = ToursEntity.builder()
                .locale(req.getLocale())
                .translationGroupId(origin.getTranslationGroupId())
                .originId(origin.getId())
                .isDefaultLocale(false)
                .title(req.getTitle())
                .slug(req.getSlug())
                .destinationId(req.getDestinationId())
                .images(req.getImages())
                .durationDays(req.getDurationDays())
                .maxPeople(req.getMaxPeople())
                .price(req.getPrice())
                .salePrice(req.getSalePrice())
                .currency(req.getCurrency() != null ? req.getCurrency() : "VND")
                .description(req.getDescription())
                .itinerary(req.getItinerary())
                .tags(req.getTags())
                .isFeatured(req.isFeatured())
                .status(TourStatus.draft)
                .seo(mapSeo(req))
                .build();

        return toursRepository.save(tour);
    }

    @Override
    public ToursEntity update(String id, ToursUpdateRequest req) {
        ToursEntity tour = toursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        if (req.getTitle() != null) tour.setTitle(req.getTitle());
        if (req.getSlug() != null) tour.setSlug(req.getSlug());
        if (req.getDestinationId() != null) tour.setDestinationId(req.getDestinationId());
        if (req.getImages() != null) tour.setImages(req.getImages());
        if (req.getDurationDays() != null) tour.setDurationDays(req.getDurationDays());
        if (req.getMaxPeople() != null) tour.setMaxPeople(req.getMaxPeople());
        if (req.getPrice() != null) tour.setPrice(req.getPrice());
        if (req.getSalePrice() != null) tour.setSalePrice(req.getSalePrice());
        if (req.getCurrency() != null) tour.setCurrency(req.getCurrency());
        if (req.getDescription() != null) tour.setDescription(req.getDescription());
        if (req.getItinerary() != null) tour.setItinerary(req.getItinerary());
        if (req.getTags() != null) tour.setTags(req.getTags());
        if (req.getIsFeatured() != null) tour.setFeatured(req.getIsFeatured());

        if (req.getSeo() != null) {
            SEOMeta seo = new SEOMeta();
            seo.setTitle(req.getSeo().getTitle());
            seo.setDescription(req.getSeo().getDescription());
            seo.setKeywords(req.getSeo().getKeywords());
            tour.setSeo(seo);
        }

        return toursRepository.save(tour);
    }

    @Override
    public ToursEntity publish(String id) {
        ToursEntity tour = toursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        tour.setStatus(TourStatus.published);
        return toursRepository.save(tour);
    }

    @Override
    public ToursEntity archive(String id) {
        ToursEntity tour = toursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        tour.setStatus(TourStatus.archived);
        return toursRepository.save(tour);
    }

    @Override
    public void delete(String id) {
        toursRepository.deleteById(id);
    }

    @Override
    public ToursEntity getBySlug(String slug, String locale) {
        return toursRepository.findBySlugAndLocale(slug, locale)
                .orElseThrow(() -> new RuntimeException("Tour not found"));
    }

    @Override
    public List<ToursEntity> getTranslations(String translationGroupId) {
        return toursRepository.findByTranslationGroupId(translationGroupId);
    }

    @Override
    public List<ToursEntity> getAll(String locale) {
        return toursRepository.findByLocale(locale);
    }

    @Override
    public List<ToursEntity> getByStatus(String locale, TourStatus status) {
        return toursRepository.findByLocaleAndStatus(locale, status);
    }

    @Override
    public List<ToursEntity> getByDestination(String destinationId, String locale) {
        return toursRepository.findByDestinationIdAndLocale(destinationId, locale);
    }

    @Override
    public List<ToursEntity> getPublishedByDestination(String destinationId, String locale) {
        return toursRepository.findByDestinationIdAndLocaleAndStatus(destinationId, locale, TourStatus.published);
    }

    @Override
    public List<ToursEntity> getFeatured(String locale) {
        return toursRepository.findByLocaleAndIsFeatured(locale, true);
    }

    @Override
    public List<ToursEntity> getFeaturedPublished(String locale) {
        return toursRepository.findByLocaleAndStatusAndIsFeatured(locale, TourStatus.published, true);
    }

    @Override
    public List<ToursEntity> getByTag(String tag, String locale) {
        return toursRepository.findByTagsContainingAndLocale(tag, locale);
    }

    @Override
    public List<ToursEntity> getPublishedByTag(String tag, String locale) {
        return toursRepository.findByTagsContainingAndLocaleAndStatus(tag, locale, TourStatus.published);
    }

    private SEOMeta mapSeo(ToursCreateRequest req) {
        if (req.getSeo() == null) return null;

        SEOMeta seo = new SEOMeta();
        seo.setTitle(req.getSeo().getTitle());
        seo.setDescription(req.getSeo().getDescription());
        seo.setKeywords(req.getSeo().getKeywords());
        return seo;
    }
}
