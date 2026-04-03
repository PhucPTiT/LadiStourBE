package com.ladi.stour.dto;

import com.ladi.stour.embedded.ItineraryDay;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ToursCreateRequest {
    @NotBlank
    private String locale;

    @NotBlank
    private String title;

    private String slug;

    @NotBlank
    private String destinationId;

    private List<String> images;

    @Positive
    private Integer durationDays;

    @Positive
    private Integer durationNights;

    @Positive
    private Integer maxPeople;

    private BigDecimal price;
    private BigDecimal salePrice;
    private String currency;

    private String description;
    private List<ItineraryDay> itinerary;

    private List<String> tags;
    private boolean isFeatured;

    private SeoRequest seo;

    @Data
    public static class SeoRequest {
        private String title;
        private String description;
        private List<String> keywords;
    }
}
