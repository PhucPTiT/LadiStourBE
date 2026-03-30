package com.ladi.stour.dto;

import com.ladi.stour.embedded.Location;
import lombok.Data;

import java.util.List;

@Data
public class DestinationsUpdateRequest {
    private String name;
    private String slug;
    private String thumbnail;
    private String banner;

    private String shortDescription;
    private String description;

    private Location location;
    private Boolean isFeatured;

    private SeoRequest seo;

    @Data
    public static class SeoRequest {
        private String title;
        private String description;
        private List<String> keywords;
    }
}
