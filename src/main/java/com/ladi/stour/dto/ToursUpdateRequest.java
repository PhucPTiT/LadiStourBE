package com.ladi.stour.dto;

import com.ladi.stour.embedded.ItineraryDay;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ToursUpdateRequest {
    private String title;
    private String slug;

    private String destinationId;
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
    private Boolean isFeatured;

    private SeoRequest seo;

    @Data
    public static class SeoRequest {
        private String title;
        private String description;
        private List<String> keywords;
    }
}
