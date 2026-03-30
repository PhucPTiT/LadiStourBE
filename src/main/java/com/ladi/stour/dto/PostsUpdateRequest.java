package com.ladi.stour.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostsUpdateRequest {
    private String title;
    private String slug;
    private String thumbnail;
    private String excerpt;
    private String contentHtml;

    private String categoryId;
    private List<String> tags;

    private SeoRequest seo;

    @Data
    public static class SeoRequest {
        private String title;
        private String description;
        private List<String> keywords;
    }
}
