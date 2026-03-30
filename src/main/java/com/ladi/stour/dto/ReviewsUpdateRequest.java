package com.ladi.stour.dto;

import lombok.Data;

@Data
public class ReviewsUpdateRequest {
    private Integer rating;
    private String comment;
    private String authorName;
    private String authorAvatar;
}
