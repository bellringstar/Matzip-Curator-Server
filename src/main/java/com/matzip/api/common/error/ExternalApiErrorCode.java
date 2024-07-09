package com.matzip.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ExternalApiErrorCode implements ErrorCodeIfs {

    EXTERNAL_API_REQUEST_FAIL(HttpStatus.SERVICE_UNAVAILABLE.value(), 1000, "외부 API 요청 실패");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
