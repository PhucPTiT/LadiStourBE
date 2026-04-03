package com.ladi.stour.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostsGetByIdRequest {
    @NotBlank(message = "id is required")
    private String id;
}
