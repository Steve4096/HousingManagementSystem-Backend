package com.example.housingmanagementsystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ConflictException.class,DuplicateException.class})
    public ResponseEntity<String> handleDuplicate(RuntimeException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }


}
