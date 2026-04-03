package com.ladi.stour.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriesCreateRequest {
    @NotBlank
    private String locale;

    @NotBlank
    private String name;

    private String slug;
}
