package com.ladi.stour.service.impl;

import com.ladi.stour.dto.DestinationsCreateRequest;
import com.ladi.stour.dto.DestinationsMultiLanguageRequest;
import com.ladi.stour.dto.DestinationsUpdateRequest;
import com.ladi.stour.dto.MessageResponse;
import com.ladi.stour.embedded.SEOMeta;
import com.ladi.stour.entity.DestinationsEntity;
import com.ladi.stour.repository.DestinationsRepository;
import com.ladi.stour.service.InterfaceDestinationsService;
import com.ladi.stour.common.SlugGenerator;
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
                .slug(resolveSlugForCreate(req.getSlug(), req.getName()))
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
                .slug(resolveSlugForCreate(req.getSlug(), req.getName()))
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
        if (req.getSlug() != null && !req.getSlug().isBlank()) {
            destination.setSlug(resolveSlugForUpdate(req.getSlug(), id));
        }
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
    public MessageResponse delete(String id) {
        DestinationsEntity destination = destinationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        // Delete all translations in the same translation group
        if (destination.getTranslationGroupId() != null) {
            List<DestinationsEntity> translations = destinationsRepository.findByTranslationGroupId(destination.getTranslationGroupId());
            destinationsRepository.deleteAll(translations);
        } else {
            destinationsRepository.deleteById(id);
        }

        return new MessageResponse("Đã xóa thành công");
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

    private SEOMeta mapSeoFromMultiLanguage(DestinationsMultiLanguageRequest.SeoRequest seoRequest) {
        if (seoRequest == null) return null;

        SEOMeta seo = new SEOMeta();
        seo.setTitle(seoRequest.getTitle());
        seo.setDescription(seoRequest.getDescription());
        seo.setKeywords(seoRequest.getKeywords());
        return seo;
    }

    private String resolveSlugForCreate(String requestedSlug, String fallbackName) {
        String baseSlug = buildBaseSlug(requestedSlug, fallbackName);
        String slug = baseSlug;
        int counter = 1;

        while (destinationsRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }

    private String resolveSlugForUpdate(String requestedSlug, String id) {
        String baseSlug = buildBaseSlug(requestedSlug, null);
        String slug = baseSlug;
        int counter = 1;

        while (destinationsRepository.existsBySlugAndIdNot(slug, id)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }

    private String buildBaseSlug(String requestedSlug, String fallbackName) {
        String source = requestedSlug != null && !requestedSlug.isBlank() ? requestedSlug : fallbackName;
        String slug = SlugGenerator.generateSlug(source);
        if (slug.isBlank()) {
            throw new RuntimeException("Unable to generate slug");
        }
        return slug;
    }

    @Override
    public MessageResponse createOrUpdateMultiLanguage(DestinationsMultiLanguageRequest req) {
        String groupId = new ObjectId().toString();

        // Create Vietnamese version (default locale)
        if (req.getVi() != null) {
            String viSlug = req.getVi().getSlug() != null && !req.getVi().getSlug().isEmpty()
                ? resolveSlugForCreate(req.getVi().getSlug(), req.getVi().getName())
                : resolveSlugForCreate(null, req.getVi().getName());

            DestinationsEntity viDestination = DestinationsEntity.builder()
                    .locale("vi")
                    .translationGroupId(groupId)
                    .originId(null)
                    .isDefaultLocale(true)
                    .name(req.getVi().getName())
                    .slug(viSlug)
                    .thumbnail(req.getThumbnail())
                    .banner(req.getBanner())
                    .shortDescription(req.getVi().getShortDescription())
                    .description(req.getVi().getDescription())
                    .location(req.getVi().getLocation())
                    .isFeatured(req.isFeatured())
                    .seo(mapSeoFromMultiLanguage(req.getVi().getSeo()))
                    .build();

            DestinationsEntity savedVi = destinationsRepository.save(viDestination);

            // Create English version (translation)
            if (req.getEn() != null) {
                String enSlug = req.getEn().getSlug() != null && !req.getEn().getSlug().isEmpty()
                    ? resolveSlugForCreate(req.getEn().getSlug(), req.getEn().getName())
                    : resolveSlugForCreate(null, req.getEn().getName());

                DestinationsEntity enDestination = DestinationsEntity.builder()
                        .locale("en")
                        .translationGroupId(groupId)
                        .originId(savedVi.getId())
                        .isDefaultLocale(false)
                        .name(req.getEn().getName())
                        .slug(enSlug)
                        .thumbnail(req.getThumbnail())
                        .banner(req.getBanner())
                        .shortDescription(req.getEn().getShortDescription())
                        .description(req.getEn().getDescription())
                        .location(req.getEn().getLocation())
                        .isFeatured(req.isFeatured())
                        .seo(mapSeoFromMultiLanguage(req.getEn().getSeo()))
                        .build();

                destinationsRepository.save(enDestination);
            }
        }

        return new MessageResponse("Tạo điểm đến thành công");
    }

    @Override
    public MessageResponse createOrUpdateMultiLanguage(String id, DestinationsMultiLanguageRequest req) {
        // Get the destination to find its translation group
        DestinationsEntity destination = destinationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        String translationGroupId = destination.getTranslationGroupId();

        // Update Vietnamese version
        if (req.getVi() != null) {
            DestinationsEntity viDestination = destinationsRepository.findByTranslationGroupIdAndLocale(translationGroupId, "vi")
                    .orElseThrow(() -> new RuntimeException("Vietnamese version not found"));

            viDestination.setName(req.getVi().getName());

            // Update slug if provided, otherwise keep existing
            if (req.getVi().getSlug() != null && !req.getVi().getSlug().isEmpty()) {
                viDestination.setSlug(resolveSlugForUpdate(req.getVi().getSlug(), viDestination.getId()));
            }

            viDestination.setShortDescription(req.getVi().getShortDescription());
            viDestination.setDescription(req.getVi().getDescription());
            if (req.getVi().getLocation() != null) {
                viDestination.setLocation(req.getVi().getLocation());
            }
            viDestination.setSeo(mapSeoFromMultiLanguage(req.getVi().getSeo()));

            destinationsRepository.save(viDestination);
        }

        // Update English version
        if (req.getEn() != null) {
            DestinationsEntity enDestination = destinationsRepository.findByTranslationGroupIdAndLocale(translationGroupId, "en")
                    .orElse(null);

            if (enDestination != null) {
                // Update existing English version
                enDestination.setName(req.getEn().getName());

                // Update slug if provided, otherwise keep existing
                if (req.getEn().getSlug() != null && !req.getEn().getSlug().isEmpty()) {
                    enDestination.setSlug(resolveSlugForUpdate(req.getEn().getSlug(), enDestination.getId()));
                }

                enDestination.setShortDescription(req.getEn().getShortDescription());
                enDestination.setDescription(req.getEn().getDescription());
                if (req.getEn().getLocation() != null) {
                    enDestination.setLocation(req.getEn().getLocation());
                }
                enDestination.setSeo(mapSeoFromMultiLanguage(req.getEn().getSeo()));

                destinationsRepository.save(enDestination);
            } else {
                // Create English version if it doesn't exist
                String enSlug = req.getEn().getSlug() != null && !req.getEn().getSlug().isEmpty()
                    ? resolveSlugForCreate(req.getEn().getSlug(), req.getEn().getName())
                    : resolveSlugForCreate(null, req.getEn().getName());

                DestinationsEntity newEnDestination = DestinationsEntity.builder()
                        .locale("en")
                        .translationGroupId(translationGroupId)
                        .originId(id)
                        .isDefaultLocale(false)
                        .name(req.getEn().getName())
                        .slug(enSlug)
                        .thumbnail(req.getThumbnail())
                        .banner(req.getBanner())
                        .shortDescription(req.getEn().getShortDescription())
                        .description(req.getEn().getDescription())
                        .location(req.getEn().getLocation())
                        .isFeatured(req.isFeatured())
                        .seo(mapSeoFromMultiLanguage(req.getEn().getSeo()))
                        .build();

                destinationsRepository.save(newEnDestination);
            }
        }

        // Update shared fields (thumbnail, banner, isFeatured)
        if (req.getThumbnail() != null || req.getBanner() != null) {
            List<DestinationsEntity> allTranslations = destinationsRepository.findByTranslationGroupId(translationGroupId);
            for (DestinationsEntity entity : allTranslations) {
                if (req.getThumbnail() != null) entity.setThumbnail(req.getThumbnail());
                if (req.getBanner() != null) entity.setBanner(req.getBanner());
                entity.setFeatured(req.isFeatured());
                destinationsRepository.save(entity);
            }
        }

        return new MessageResponse("Cập nhật điểm đến thành công");
    }
}
