package com.hodolog.controller.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiCode {
    OK("OK"),
    VALIDATION_ERROR("VALIDATION_ERROR");

    private final String code;

}
