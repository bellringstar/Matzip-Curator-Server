package com.matzip.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserPreferenceErrorCode implements ErrorCodeIfs {

    USER_PREFERENCE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 3000, "사용자 선호도가 존재하지 않습니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
