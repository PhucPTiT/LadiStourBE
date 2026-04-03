package com.ladi.stour.dto;

import com.ladi.stour.embedded.ItineraryDay;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ToursMultiLanguageRequest {
    @Valid
    private LocaleData vi;

    @Valid
    private LocaleData en;

    private String destinationId;

    private List<String> images;
    private boolean isFeatured;

    @Data
    public static class LocaleData {
        @NotBlank
        private String title;

        private String slug;

        private String description;

        @Positive
        private Integer durationDays;

        @Positive
        private Integer durationNights;

        @Positive
        private Integer maxPeople;

        private BigDecimal price;
        private BigDecimal salePrice;
        private String currency;

        private List<ItineraryDay> itinerary;
        private List<String> tags;

        @Valid
        private SeoRequest seo;
    }

    @Data
    public static class SeoRequest {
        private String title;
        private String description;
        private List<String> keywords;
    }
}
