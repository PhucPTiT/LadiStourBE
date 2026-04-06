package com.ladi.stour.dto;

import com.ladi.stour.embedded.LocalizedContent;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class HomeHeroSectionUpdateRequest {
    private Boolean autoPlay;
    private Integer autoPlayDelayMs;

    @Valid
    private List<SlideRequest> slides;

    @Data
    public static class SlideRequest {
        @Valid
        private LocalizedContent title;

        @Valid
        private LocalizedContent subtitle;

        @Valid
        private LocalizedContent label;

        @Valid
        private LocalizedContent cta;

        private String image;
        private String href;
        private Integer sortOrder;
        private Boolean active;
    }
}
