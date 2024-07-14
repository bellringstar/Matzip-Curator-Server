package com.matzip.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCodeIfs {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 5000, "유저가 존재하지 않습니다."),
    DUPLICATE_LOGIN_ID(HttpStatus.NOT_FOUND.value(), 5001, "이미 존재하는 id입니다."),
    DUPLICATE_EMAIL(HttpStatus.NOT_FOUND.value(), 5002, "이미 존재하는 email입니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
