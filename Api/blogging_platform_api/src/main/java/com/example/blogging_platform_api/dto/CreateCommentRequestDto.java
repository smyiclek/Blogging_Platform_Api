package com.example.blogging_platform_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequestDto {

    @NotBlank(message = "Comment content cannot be blank")
    @Size(min = 1, max = 1000, message = "Comment content must be between 1 and 1000 characters")
    private String content;

    @NotNull(message = "Post ID cannot be null for a comment")
    private Long postId;

    @NotNull(message = "Author ID cannot be null for a comment")
    private Long authorId;
}