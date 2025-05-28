package com.example.blogging_platform_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CreatePostRequestDto {

    @NotBlank(message = "Post title cannot be blank")
    @Size(min = 5, max = 255, message = "Post title must be between 5 and 255 characters")
    private String title;

    @NotBlank(message = "Post content cannot be blank")
    private String content;

    @NotNull(message = "Author ID cannot be null")
    private Long authorId;

    private Long categoryId;

    private Set<Long> tagIds;
}