package com.ladi.stour.embedded;

import lombok.Data;

@Data
public class ServiceHighlightItem {
    private String icon;
    private LocalizedContent title;
    private LocalizedContent text;
    private Integer sortOrder;
    private Boolean active;
}
