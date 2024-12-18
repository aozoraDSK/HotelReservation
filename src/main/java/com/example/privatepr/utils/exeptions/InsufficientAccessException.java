package com.example.privatepr.utils.exeptions;

public class InsufficientAccessException extends RuntimeException {
    public InsufficientAccessException(String message) {
        super(message);
    }
}
