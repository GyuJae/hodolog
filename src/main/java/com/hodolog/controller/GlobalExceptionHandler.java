package com.hodolog.controller;

import com.hodolog.exception.PostNotFoundException;
import com.hodolog.response.ApiCode;
import com.hodolog.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> validation = new LinkedHashMap<>();

        for (FieldError fe : e.getBindingResult().getFieldErrors()) {
            validation.put(fe.getField(), fe.getDefaultMessage());
        }

        ErrorResponse body = ErrorResponse.builder()
                .code(ApiCode.VALIDATION_ERROR.getCode())
                .message("요청 값이 올바르지 않습니다.")
                .validation(validation)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePostNotFound(PostNotFoundException e) {
        ErrorResponse body = ErrorResponse.builder()
                .code(ApiCode.NOT_FOUND.getCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
