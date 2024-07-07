package com.matzip.api.common.error.entity;

import com.matzip.api.common.error.ErrorCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum EntityErrorCode implements ErrorCodeIfs {

    VALIDATION_FAIL(HttpStatus.BAD_REQUEST.value(), 2000, "유효한 필드값이 아닙니다."),;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
