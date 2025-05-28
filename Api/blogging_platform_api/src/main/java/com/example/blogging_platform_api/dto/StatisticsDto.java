package com.example.blogging_platform_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatisticsDto {
    private long totalAuthors;
    private long totalPosts;
    private long totalCategories;
    private long totalTags;
    private long totalComments;
}