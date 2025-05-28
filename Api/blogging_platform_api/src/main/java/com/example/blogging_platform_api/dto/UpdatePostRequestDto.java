package com.example.blogging_platform_api.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdatePostRequestDto {

    @Size(min = 5, max = 255, message = "Post title must be between 5 and 255 characters if provided")
    private String title;

    private String content;
}