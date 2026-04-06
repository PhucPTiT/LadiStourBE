package com.ladi.stour.dto;

import com.ladi.stour.embedded.LocalizedContent;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class ServiceHighlightsSectionUpdateRequest {
    @Valid
    private LocalizedContent eyebrow;

    @Valid
    private LocalizedContent title;

    @Valid
    private LocalizedContent description;

    @Valid
    private List<ItemRequest> items;

    @Data
    public static class ItemRequest {
        private String icon;

        @Valid
        private LocalizedContent title;

        @Valid
        private LocalizedContent text;

        private Integer sortOrder;
        private Boolean active;
    }
}
