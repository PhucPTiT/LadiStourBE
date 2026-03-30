package com.ladi.stour.dto;

import com.ladi.stour.enums.ReviewType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewsCreateRequest {
    @NotNull
    private ReviewType type;

    private String tourId;
    private String companyId;

    private String locale;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank
    private String comment;

    @NotBlank
    private String authorName;

    private String authorAvatar;
}
