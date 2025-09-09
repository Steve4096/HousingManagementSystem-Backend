package com.example.housingmanagementsystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<String> handleDuplicate(DuplicateException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }


}
