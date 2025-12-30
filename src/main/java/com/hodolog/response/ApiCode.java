package com.hodolog.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.tools.Diagnostic;

@Getter
@RequiredArgsConstructor
public enum ApiCode {
    OK("OK"),
    VALIDATION_ERROR("VALIDATION_ERROR"),
    NOT_FOUND("NOT_FOUND");

    private final String code;

}
