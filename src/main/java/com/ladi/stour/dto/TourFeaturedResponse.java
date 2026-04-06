package com.ladi.stour.dto;

import com.ladi.stour.embedded.ItineraryDay;
import com.ladi.stour.embedded.SEOMeta;
import com.ladi.stour.entity.DestinationsEntity;
import com.ladi.stour.entity.ToursEntity;
import com.ladi.stour.enums.TourStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class TourFeaturedResponse {
    private String id;
    private String locale;
    private String translationGroupId;
    private String originId;
    private boolean isDefaultLocale;
    private String title;
    private String slug;
    private DestinationsEntity destination;
    private List<String> images;
    private Integer durationDays;
    private Integer durationNights;
    private Integer maxPeople;
    private BigDecimal price;
    private BigDecimal salePrice;
    private String currency;
    private String description;
    private List<ItineraryDay> itinerary;
    private List<String> tags;
    private boolean isFeatured;
    private TourStatus status;
    private SEOMeta seo;
    private Instant createdAt;
    private Instant updatedAt;

    public static TourFeaturedResponse from(ToursEntity tour, DestinationsEntity destination) {
        return TourFeaturedResponse.builder()
                .id(tour.getId())
                .locale(tour.getLocale())
                .translationGroupId(tour.getTranslationGroupId())
                .originId(tour.getOriginId())
                .isDefaultLocale(tour.isDefaultLocale())
                .title(tour.getTitle())
                .slug(tour.getSlug())
                .destination(destination)
                .images(tour.getImages())
                .durationDays(tour.getDurationDays())
                .durationNights(tour.getDurationNights())
                .maxPeople(tour.getMaxPeople())
                .price(tour.getPrice())
                .salePrice(tour.getSalePrice())
                .currency(tour.getCurrency())
                .description(tour.getDescription())
                .itinerary(tour.getItinerary())
                .tags(tour.getTags())
                .isFeatured(tour.isFeatured())
                .status(tour.getStatus())
                .seo(tour.getSeo())
                .createdAt(tour.getCreatedAt())
                .updatedAt(tour.getUpdatedAt())
                .build();
    }
}
