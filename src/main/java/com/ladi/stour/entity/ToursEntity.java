package com.ladi.stour.entity;

import com.ladi.stour.common.BaseDocument;
import com.ladi.stour.embedded.ItineraryDay;
import com.ladi.stour.embedded.SEOMeta;
import com.ladi.stour.enums.TourStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "tours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToursEntity extends BaseDocument {
    @Id
    private String id;

    private String locale;
    private String translationGroupId;
    private String originId;
    private boolean isDefaultLocale;

    private String title;
    private String slug;

    private String destinationId;
    private List<String> images;

    private Integer durationDays;
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
}
