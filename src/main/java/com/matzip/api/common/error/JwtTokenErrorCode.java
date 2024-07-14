package com.matzip.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum JwtTokenErrorCode implements ErrorCodeIfs {

    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST.value(), 4000, "유효하지 않은 리프레쉬 토큰입니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
