package com.url.shortner.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomUrl {

    @NotBlank
    private String originalUrl;
    private String customUrl;

}
