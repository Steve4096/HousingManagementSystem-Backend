package com.example.housingmanagementsystem.Exceptions;


public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
