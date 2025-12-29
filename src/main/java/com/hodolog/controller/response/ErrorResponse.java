package com.hodolog.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    private Map<String, String> validation;
}
