package com.example.pfa_authentification.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    // autres champs si nécessaire

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}