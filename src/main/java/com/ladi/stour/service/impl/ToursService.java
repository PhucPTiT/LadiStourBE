package com.ladi.stour.service.impl;

import com.ladi.stour.common.SlugGenerator;
import com.ladi.stour.dto.MessageResponse;
import com.ladi.stour.dto.ToursCreateRequest;
import com.ladi.stour.dto.ToursMultiLanguageRequest;
import com.ladi.stour.dto.ToursUpdateRequest;
import com.ladi.stour.embedded.SEOMeta;
import com.ladi.stour.entity.DestinationsEntity;
import com.ladi.stour.entity.ToursEntity;
import com.ladi.stour.enums.TourStatus;
import com.ladi.stour.repository.DestinationsRepository;
import com.ladi.stour.repository.ToursRepository;
import com.ladi.stour.service.InterfaceToursService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ToursService implements InterfaceToursService {
    private final ToursRepository toursRepository;
    private final DestinationsRepository destinationsRepository;

    @Override
    public ToursEntity createDefault(ToursCreateRequest req) {
        String groupId = new ObjectId().toString();

        ToursEntity tour = ToursEntity.builder()
                .locale(req.getLocale())
                .translationGroupId(groupId)
                .originId(null)
                .isDefaultLocale(true)
                .title(req.getTitle())
                .slug(resolveSlugForCreate(req.getSlug(), req.getTitle()))
                .destinationId(req.getDestinationId())
                .images(req.getImages())
                .durationDays(req.getDurationDays())
                .durationNights(req.getDurationNights())
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
                .slug(resolveSlugForCreate(req.getSlug(), req.getTitle()))
                .destinationId(req.getDestinationId())
                .images(req.getImages())
                .durationDays(req.getDurationDays())
                .durationNights(req.getDurationNights())
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
        if (req.getSlug() != null && !req.getSlug().isBlank()) {
            tour.setSlug(resolveSlugForUpdate(req.getSlug(), id));
        }
        if (req.getDestinationId() != null) tour.setDestinationId(req.getDestinationId());
        if (req.getImages() != null) tour.setImages(req.getImages());
        if (req.getDurationDays() != null) tour.setDurationDays(req.getDurationDays());
        if (req.getDurationNights() != null) tour.setDurationNights(req.getDurationNights());
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
    public MessageResponse delete(String id) {
        ToursEntity tour = toursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        // Delete all translations in the same translation group
        if (tour.getTranslationGroupId() != null) {
            List<ToursEntity> translations = toursRepository.findByTranslationGroupId(tour.getTranslationGroupId());
            toursRepository.deleteAll(translations);
        } else {
            toursRepository.deleteById(id);
        }

        return new MessageResponse("Đã xóa thành công");
    }

    @Override
    public MessageResponse createOrUpdateMultiLanguage(ToursMultiLanguageRequest req) {
        String groupId = new ObjectId().toString();
        Map<String, String> destinationIdsByLocale = resolveDestinationIdsByLocale(req.getDestinationId());

        // Create Vietnamese version (default locale)
        if (req.getVi() != null) {
            String viSlug = req.getVi().getSlug() != null && !req.getVi().getSlug().isEmpty()
                ? resolveSlugForCreate(req.getVi().getSlug(), req.getVi().getTitle())
                : resolveSlugForCreate(null, req.getVi().getTitle());

            ToursEntity viTour = ToursEntity.builder()
                    .locale("vi")
                    .translationGroupId(groupId)
                    .originId(null)
                    .isDefaultLocale(true)
                    .title(req.getVi().getTitle())
                    .slug(viSlug)
                    .destinationId(resolveDestinationIdForLocale(destinationIdsByLocale, "vi"))
                    .images(req.getImages())
                    .durationDays(req.getVi().getDurationDays())
                    .durationNights(req.getVi().getDurationNights())
                    .maxPeople(req.getVi().getMaxPeople())
                    .price(req.getVi().getPrice())
                    .salePrice(req.getVi().getSalePrice())
                    .currency(req.getVi().getCurrency() != null ? req.getVi().getCurrency() : "VND")
                    .description(req.getVi().getDescription())
                    .itinerary(req.getVi().getItinerary())
                    .tags(req.getVi().getTags())
                    .isFeatured(req.isFeatured())
                    .status(TourStatus.draft)
                    .seo(mapSeoFromMultiLanguage(req.getVi().getSeo()))
                    .build();

            ToursEntity savedVi = toursRepository.save(viTour);

            // Create English version (translation)
            if (req.getEn() != null) {
                String enSlug = req.getEn().getSlug() != null && !req.getEn().getSlug().isEmpty()
                    ? resolveSlugForCreate(req.getEn().getSlug(), req.getEn().getTitle())
                    : resolveSlugForCreate(null, req.getEn().getTitle());

                ToursEntity enTour = ToursEntity.builder()
                        .locale("en")
                        .translationGroupId(groupId)
                        .originId(savedVi.getId())
                        .isDefaultLocale(false)
                        .title(req.getEn().getTitle())
                        .slug(enSlug)
                        .destinationId(resolveDestinationIdForLocale(destinationIdsByLocale, "en"))
                        .images(req.getImages())
                        .durationDays(req.getEn().getDurationDays())
                        .durationNights(req.getEn().getDurationNights())
                        .maxPeople(req.getEn().getMaxPeople())
                        .price(req.getEn().getPrice())
                        .salePrice(req.getEn().getSalePrice())
                        .currency(req.getEn().getCurrency() != null ? req.getEn().getCurrency() : "VND")
                        .description(req.getEn().getDescription())
                        .itinerary(req.getEn().getItinerary())
                        .tags(req.getEn().getTags())
                        .isFeatured(req.isFeatured())
                        .status(TourStatus.draft)
                        .seo(mapSeoFromMultiLanguage(req.getEn().getSeo()))
                        .build();

                toursRepository.save(enTour);
            }
        }

        return new MessageResponse("Tạo tour thành công");
    }

    @Override
    public MessageResponse createOrUpdateMultiLanguage(String id, ToursMultiLanguageRequest req) {
        // Get the tour to find its translation group
        ToursEntity tour = toursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        String translationGroupId = tour.getTranslationGroupId();
        Map<String, String> destinationIdsByLocale = resolveDestinationIdsByLocale(req.getDestinationId());

        // Update Vietnamese version
        if (req.getVi() != null) {
            ToursEntity viTour = toursRepository.findByTranslationGroupIdAndLocale(translationGroupId, "vi")
                    .orElseThrow(() -> new RuntimeException("Vietnamese version not found"));

            viTour.setTitle(req.getVi().getTitle());

            // Update slug if provided, otherwise keep existing
            if (req.getVi().getSlug() != null && !req.getVi().getSlug().isEmpty()) {
                viTour.setSlug(resolveSlugForUpdate(req.getVi().getSlug(), viTour.getId()));
            }

            viTour.setDescription(req.getVi().getDescription());
            if (req.getVi().getDurationDays() != null) viTour.setDurationDays(req.getVi().getDurationDays());
            if (req.getVi().getDurationNights() != null) viTour.setDurationNights(req.getVi().getDurationNights());
            if (req.getVi().getMaxPeople() != null) viTour.setMaxPeople(req.getVi().getMaxPeople());
            if (req.getVi().getPrice() != null) viTour.setPrice(req.getVi().getPrice());
            if (req.getVi().getSalePrice() != null) viTour.setSalePrice(req.getVi().getSalePrice());
            if (req.getVi().getCurrency() != null) viTour.setCurrency(req.getVi().getCurrency());
            if (req.getVi().getItinerary() != null) viTour.setItinerary(req.getVi().getItinerary());
            if (req.getVi().getTags() != null) viTour.setTags(req.getVi().getTags());
            if (!destinationIdsByLocale.isEmpty()) {
                viTour.setDestinationId(resolveDestinationIdForLocale(destinationIdsByLocale, "vi"));
            }
            viTour.setSeo(mapSeoFromMultiLanguage(req.getVi().getSeo()));

            toursRepository.save(viTour);
        }

        // Update English version
        if (req.getEn() != null) {
            ToursEntity enTour = toursRepository.findByTranslationGroupIdAndLocale(translationGroupId, "en")
                    .orElse(null);

            if (enTour != null) {
                // Update existing English version
                enTour.setTitle(req.getEn().getTitle());

                // Update slug if provided, otherwise keep existing
                if (req.getEn().getSlug() != null && !req.getEn().getSlug().isEmpty()) {
                    enTour.setSlug(resolveSlugForUpdate(req.getEn().getSlug(), enTour.getId()));
                }

                enTour.setDescription(req.getEn().getDescription());
                if (req.getEn().getDurationDays() != null) enTour.setDurationDays(req.getEn().getDurationDays());
                if (req.getEn().getDurationNights() != null) enTour.setDurationNights(req.getEn().getDurationNights());
                if (req.getEn().getMaxPeople() != null) enTour.setMaxPeople(req.getEn().getMaxPeople());
                if (req.getEn().getPrice() != null) enTour.setPrice(req.getEn().getPrice());
                if (req.getEn().getSalePrice() != null) enTour.setSalePrice(req.getEn().getSalePrice());
                if (req.getEn().getCurrency() != null) enTour.setCurrency(req.getEn().getCurrency());
                if (req.getEn().getItinerary() != null) enTour.setItinerary(req.getEn().getItinerary());
                if (req.getEn().getTags() != null) enTour.setTags(req.getEn().getTags());
                if (!destinationIdsByLocale.isEmpty()) {
                    enTour.setDestinationId(resolveDestinationIdForLocale(destinationIdsByLocale, "en"));
                }
                enTour.setSeo(mapSeoFromMultiLanguage(req.getEn().getSeo()));

                toursRepository.save(enTour);
            } else {
                // Create English version if it doesn't exist
                String enSlug = req.getEn().getSlug() != null && !req.getEn().getSlug().isEmpty()
                    ? resolveSlugForCreate(req.getEn().getSlug(), req.getEn().getTitle())
                    : resolveSlugForCreate(null, req.getEn().getTitle());

                ToursEntity newEnTour = ToursEntity.builder()
                        .locale("en")
                        .translationGroupId(translationGroupId)
                        .originId(id)
                        .isDefaultLocale(false)
                        .title(req.getEn().getTitle())
                        .slug(enSlug)
                        .destinationId(resolveDestinationIdForLocale(destinationIdsByLocale, "en"))
                        .images(req.getImages())
                        .durationDays(req.getEn().getDurationDays())
                        .durationNights(req.getEn().getDurationNights())
                        .maxPeople(req.getEn().getMaxPeople())
                        .price(req.getEn().getPrice())
                        .salePrice(req.getEn().getSalePrice())
                        .currency(req.getEn().getCurrency() != null ? req.getEn().getCurrency() : "VND")
                        .description(req.getEn().getDescription())
                        .itinerary(req.getEn().getItinerary())
                        .tags(req.getEn().getTags())
                        .isFeatured(req.isFeatured())
                        .status(TourStatus.draft)
                        .seo(mapSeoFromMultiLanguage(req.getEn().getSeo()))
                        .build();

                toursRepository.save(newEnTour);
            }
        }

        // Update shared fields and align destination by locale
        if (req.getImages() != null || hasText(req.getDestinationId())) {
            List<ToursEntity> allTranslations = toursRepository.findByTranslationGroupId(translationGroupId);
            for (ToursEntity entity : allTranslations) {
                if (req.getImages() != null) entity.setImages(req.getImages());
                if (!destinationIdsByLocale.isEmpty()) {
                    entity.setDestinationId(resolveDestinationIdForLocale(destinationIdsByLocale, entity.getLocale()));
                }
                entity.setFeatured(req.isFeatured());
                toursRepository.save(entity);
            }
        }

        return new MessageResponse("Cập nhật tour thành công");
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

    private SEOMeta mapSeoFromMultiLanguage(ToursMultiLanguageRequest.SeoRequest seoRequest) {
        if (seoRequest == null) return null;

        SEOMeta seo = new SEOMeta();
        seo.setTitle(seoRequest.getTitle());
        seo.setDescription(seoRequest.getDescription());
        seo.setKeywords(seoRequest.getKeywords());
        return seo;
    }

    private String resolveSlugForCreate(String requestedSlug, String fallbackTitle) {
        String baseSlug = buildBaseSlug(requestedSlug, fallbackTitle);
        String slug = baseSlug;
        int counter = 1;

        while (toursRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }

    private String resolveSlugForUpdate(String requestedSlug, String id) {
        String baseSlug = buildBaseSlug(requestedSlug, null);
        String slug = baseSlug;
        int counter = 1;

        while (toursRepository.existsBySlugAndIdNot(slug, id)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }

    private String buildBaseSlug(String requestedSlug, String fallbackTitle) {
        String source = requestedSlug != null && !requestedSlug.isBlank() ? requestedSlug : fallbackTitle;
        String slug = SlugGenerator.generateSlug(source);
        if (slug.isBlank()) {
            throw new RuntimeException("Unable to generate slug");
        }
        return slug;
    }

    private Map<String, String> resolveDestinationIdsByLocale(String destinationId) {
        if (!hasText(destinationId)) {
            return Map.of();
        }

        DestinationsEntity destination = destinationsRepository.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        List<DestinationsEntity> destinationsInGroup = destination.getTranslationGroupId() != null
                ? destinationsRepository.findByTranslationGroupId(destination.getTranslationGroupId())
                : List.of(destination);

        Map<String, String> destinationIdsByLocale = new HashMap<>();
        for (DestinationsEntity entity : destinationsInGroup) {
            if (entity.getLocale() != null && entity.getId() != null) {
                destinationIdsByLocale.put(entity.getLocale(), entity.getId());
            }
        }

        destinationIdsByLocale.putIfAbsent("vi", destination.getId());
        return destinationIdsByLocale;
    }

    private String resolveDestinationIdForLocale(Map<String, String> destinationIdsByLocale, String locale) {
        if (destinationIdsByLocale == null || destinationIdsByLocale.isEmpty()) {
            return null;
        }

        return destinationIdsByLocale.getOrDefault(locale, destinationIdsByLocale.get("vi"));
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
