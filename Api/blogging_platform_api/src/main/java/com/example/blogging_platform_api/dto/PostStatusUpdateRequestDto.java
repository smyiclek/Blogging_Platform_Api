package com.example.blogging_platform_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostStatusUpdateRequestDto {

    @NotNull(message = "Status cannot be null") // Durumun null olmaması gerektiğini belirtiyoruz
    private Boolean status; // true veya false değeri alacak
}