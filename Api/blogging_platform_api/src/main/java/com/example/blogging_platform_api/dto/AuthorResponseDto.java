package com.example.blogging_platform_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuthorResponseDto {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime registrationDate;
}