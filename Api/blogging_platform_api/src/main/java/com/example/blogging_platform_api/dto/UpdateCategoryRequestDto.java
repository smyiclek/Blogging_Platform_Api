package com.example.blogging_platform_api.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategoryRequestDto {

    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters if provided")
    private String name;
    @Size(max = 255, message = "Category description can be at most 255 characters if provided")
    private String description;
}