package com.techlab.ecommerce_api.models.dto;

public record ErrorResponseRecordDTO(String title, String message, Long timestamp) {
    public ErrorResponseRecordDTO(String title, String message) {
        this(title, message, System.currentTimeMillis());
    }
}