package com.techlab.ecommerce_api.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDTO {
    private String title;
    private String message;
    private Long timestamp;
    
    public ErrorResponseDTO(String title, String message) {
        this.title = title;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}