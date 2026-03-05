package com.demo_user_service.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(LoginAlreadyInUseException.class)
    public ResponseEntity<?> handleLoginAlreadyInUseException(LoginAlreadyInUseException ex){
        Map<String, String> body = Map.of("error", ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

}