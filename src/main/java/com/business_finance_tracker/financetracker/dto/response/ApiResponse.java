package com.business_finance_tracker.financetracker.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// This wrap every response from the API
// Client always got consistent structure { "success": true, "message": "...", "data": {...}, "timestamp": "..." }

@Getter
@Setter
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;                 // hole any response type - T
    private LocalDateTime timestamp;

    // Static factory method - easy to create response
    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.message = message;
        response.data = data;
        response.timestamp = LocalDateTime.now();
        return response;
    }

    public static <T> ApiResponse error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        response.data = null;
        response.timestamp = LocalDateTime.now();
        return response;
    }
}
