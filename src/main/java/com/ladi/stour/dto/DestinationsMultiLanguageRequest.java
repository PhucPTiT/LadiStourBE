package com.ladi.stour.dto;

import com.ladi.stour.embedded.Location;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class DestinationsMultiLanguageRequest {
    @Valid
    private LocaleData vi;

    @Valid
    private LocaleData en;

    private String thumbnail;
    private String banner;
    private boolean isFeatured;

    @Data
    public static class LocaleData {
        @NotBlank
        private String name;

        private String slug;

        private String shortDescription;
        private String description;

        @Valid
        private Location location;

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
