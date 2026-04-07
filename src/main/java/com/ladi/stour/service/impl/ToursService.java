package com.ladi.stour.service.impl;

import com.ladi.stour.common.SlugGenerator;
import com.ladi.stour.dto.MessageResponse;
import com.ladi.stour.dto.TourFeaturedResponse;
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
        String sharedSlug = resolveSlugForGroup(req.getSlug(), req.getTitle(), groupId);

        ToursEntity tour = ToursEntity.builder()
                .locale(req.getLocale())
                .translationGroupId(groupId)
                .originId(null)
                .isDefaultLocale(true)
                .title(req.getTitle())
                .slug(sharedSlug)
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
                .status(req.getStatus() != null ? req.getStatus() : TourStatus.draft)
                .seo(mapSeo(req))
                .build();

        return toursRepository.save(tour);
    }

    @Override
    public ToursEntity createTranslation(String originTourId, ToursCreateRequest req) {
        ToursEntity origin = toursRepository.findById(originTourId)
                .orElseThrow(() -> new RuntimeException("Origin tour not found"));
        String sharedSlug = hasText(req.getSlug())
                ? resolveSlugForGroup(req.getSlug(), req.getTitle(), origin.getTranslationGroupId())
                : origin.getSlug();

        syncSlugAcrossTranslationGroup(origin.getTranslationGroupId(), sharedSlug);

        ToursEntity tour = ToursEntity.builder()
                .locale(req.getLocale())
                .translationGroupId(origin.getTranslationGroupId())
                .originId(origin.getId())
                .isDefaultLocale(false)
                .title(req.getTitle())
                .slug(sharedSlug)
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
                .status(req.getStatus() != null ? req.getStatus() : TourStatus.draft)
                .seo(mapSeo(req))
                .build();

        return toursRepository.save(tour);
    }

    @Override
    public ToursEntity update(String id, ToursUpdateRequest req) {
        ToursEntity tour = toursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));
        String translationGroupId = tour.getTranslationGroupId();

        if (req.getTitle() != null) tour.setTitle(req.getTitle());
        if (req.getSlug() != null && !req.getSlug().isBlank()) {
            String sharedSlug = resolveSlugForGroup(req.getSlug(), tour.getTitle(), translationGroupId);
            syncSlugAcrossTranslationGroup(translationGroupId, sharedSlug);
            tour.setSlug(sharedSlug);
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
        if (req.getStatus() != null) tour.setStatus(req.getStatus());

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
        String sharedSlug = resolveSlugForGroup(
                firstNonBlank(
                        req.getEn() != null ? req.getEn().getSlug() : null,
                        req.getVi() != null ? req.getVi().getSlug() : null
                ),
                firstNonBlank(
                        req.getEn() != null ? req.getEn().getTitle() : null,
                        req.getVi() != null ? req.getVi().getTitle() : null
                ),
                groupId
        );

        // Create Vietnamese version (default locale)
        if (req.getVi() != null) {
            ToursEntity viTour = ToursEntity.builder()
                    .locale("vi")
                    .translationGroupId(groupId)
                    .originId(null)
                    .isDefaultLocale(true)
                    .title(req.getVi().getTitle())
                    .slug(sharedSlug)
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
                    .isFeatured(Boolean.TRUE.equals(req.getIsFeatured()))
                    .status(req.getStatus() != null ? req.getStatus() : TourStatus.draft)
                    .seo(mapSeoFromMultiLanguage(req.getVi().getSeo()))
                    .build();

            ToursEntity savedVi = toursRepository.save(viTour);

            // Create English version (translation)
            if (req.getEn() != null) {
                ToursEntity enTour = ToursEntity.builder()
                        .locale("en")
                        .translationGroupId(groupId)
                        .originId(savedVi.getId())
                        .isDefaultLocale(false)
                        .title(req.getEn().getTitle())
                        .slug(sharedSlug)
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
                        .isFeatured(Boolean.TRUE.equals(req.getIsFeatured()))
                        .status(req.getStatus() != null ? req.getStatus() : TourStatus.draft)
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
        String sharedSlug = resolveSlugForGroup(
                firstNonBlank(
                        req.getEn() != null ? req.getEn().getSlug() : null,
                        req.getVi() != null ? req.getVi().getSlug() : null,
                        tour.getSlug()
                ),
                firstNonBlank(
                        req.getEn() != null ? req.getEn().getTitle() : null,
                        req.getVi() != null ? req.getVi().getTitle() : null,
                        tour.getTitle()
                ),
                translationGroupId
        );

        syncSlugAcrossTranslationGroup(translationGroupId, sharedSlug);

        // Update Vietnamese version
        if (req.getVi() != null) {
            ToursEntity viTour = toursRepository.findByTranslationGroupIdAndLocale(translationGroupId, "vi")
                    .orElseThrow(() -> new RuntimeException("Vietnamese version not found"));

            viTour.setTitle(req.getVi().getTitle());
            viTour.setSlug(sharedSlug);

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
                enTour.setSlug(sharedSlug);

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
                ToursEntity newEnTour = ToursEntity.builder()
                        .locale("en")
                        .translationGroupId(translationGroupId)
                        .originId(id)
                        .isDefaultLocale(false)
                        .title(req.getEn().getTitle())
                        .slug(sharedSlug)
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
                        .isFeatured(Boolean.TRUE.equals(req.getIsFeatured()))
                        .status(req.getStatus() != null ? req.getStatus() : TourStatus.draft)
                        .seo(mapSeoFromMultiLanguage(req.getEn().getSeo()))
                        .build();

                toursRepository.save(newEnTour);
            }
        }

        // Update shared fields and align destination by locale
        if (req.getImages() != null || hasText(req.getDestinationId()) || req.getIsFeatured() != null || req.getStatus() != null) {
            List<ToursEntity> allTranslations = toursRepository.findByTranslationGroupId(translationGroupId);
            for (ToursEntity entity : allTranslations) {
                if (req.getImages() != null) entity.setImages(req.getImages());
                if (!destinationIdsByLocale.isEmpty()) {
                    entity.setDestinationId(resolveDestinationIdForLocale(destinationIdsByLocale, entity.getLocale()));
                }
                if (req.getIsFeatured() != null) entity.setFeatured(req.getIsFeatured());
                if (req.getStatus() != null) entity.setStatus(req.getStatus());
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
    public List<TourFeaturedResponse> getFeatured(String locale) {
        return toursRepository.findByLocaleAndStatusAndIsFeatured(locale, TourStatus.published, true).stream()
                .map(this::mapToFeaturedResponse)
                .toList();
    }

    @Override
    public List<TourFeaturedResponse> getFeaturedPublished(String locale) {
        return toursRepository.findByLocaleAndStatusAndIsFeatured(locale, TourStatus.published, true).stream()
                .map(this::mapToFeaturedResponse)
                .toList();
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

    private String resolveSlugForGroup(String requestedSlug, String fallbackTitle, String translationGroupId) {
        String baseSlug = buildBaseSlug(requestedSlug, fallbackTitle);
        String slug = baseSlug;
        int counter = 1;

        while (slugExistsInAnotherGroup(slug, translationGroupId)) {
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

    private boolean slugExistsInAnotherGroup(String slug, String translationGroupId) {
        return toursRepository.findBySlug(slug).stream()
                .anyMatch(entity -> !sameTranslationGroup(entity.getTranslationGroupId(), translationGroupId));
    }

    private boolean sameTranslationGroup(String left, String right) {
        if (left == null || right == null) {
            return left == null && right == null;
        }
        return left.equals(right);
    }

    private void syncSlugAcrossTranslationGroup(String translationGroupId, String slug) {
        if (!hasText(translationGroupId) || !hasText(slug)) {
            return;
        }

        List<ToursEntity> translations = toursRepository.findByTranslationGroupId(translationGroupId);
        for (ToursEntity entity : translations) {
            if (!slug.equals(entity.getSlug())) {
                entity.setSlug(slug);
                toursRepository.save(entity);
            }
        }
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (hasText(value)) {
                return value;
            }
        }
        return null;
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

    private TourFeaturedResponse mapToFeaturedResponse(ToursEntity tour) {
        DestinationsEntity destination = null;
        if (hasText(tour.getDestinationId())) {
            destination = destinationsRepository.findById(tour.getDestinationId()).orElse(null);
        }

        return TourFeaturedResponse.from(tour, destination);
    }
}
