package com.example.blogging_platform_api.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTagRequestDto {

    @Size(min = 2, max = 50, message = "Tag name must be between 2 and 50 characters if provided")
    private String name;
}