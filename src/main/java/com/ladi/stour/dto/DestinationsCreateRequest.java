package com.ladi.stour.dto;

import com.ladi.stour.embedded.Location;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class DestinationsCreateRequest {
    @NotBlank
    private String locale;

    @NotBlank
    private String name;

    @NotBlank
    private String slug;

    private String thumbnail;
    private String banner;

    private String shortDescription;
    private String description;

    private Location location;
    private boolean isFeatured;

    private SeoRequest seo;

    @Data
    public static class SeoRequest {
        private String title;
        private String description;
        private List<String> keywords;
    }
}
