package com.example.blogging_platform_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime publicationDate;
    private LocalDateTime lastUpdatedDate;
    private boolean isPublished;
    private boolean isFeatured;
    private AuthorResponseDto author;
    private CategoryResponseDto category;
    private Set<TagResponseDto> tags;
}