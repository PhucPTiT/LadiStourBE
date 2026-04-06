package com.ladi.stour.embedded;

import lombok.Data;

@Data
public class HeroSlideItem {
    private LocalizedContent title;
    private LocalizedContent subtitle;
    private LocalizedContent label;
    private LocalizedContent cta;
    private String image;
    private String href;
    private Integer sortOrder;
    private Boolean active;
}
