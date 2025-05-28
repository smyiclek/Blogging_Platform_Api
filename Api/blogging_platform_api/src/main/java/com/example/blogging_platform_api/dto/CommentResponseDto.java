package com.example.blogging_platform_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime creationDate;
    private Long postId;
    private AuthorResponseDto author;
}