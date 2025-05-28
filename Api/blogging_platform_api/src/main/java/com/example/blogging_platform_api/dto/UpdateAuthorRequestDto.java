package com.example.blogging_platform_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAuthorRequestDto {


    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters if provided")
    private String name;

    @Email(message = "Please provide a valid email address if provided")
    @Size(max = 100, message = "Email can be at most 100 characters if provided")
    private String email;
}