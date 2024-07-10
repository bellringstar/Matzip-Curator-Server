package com.matzip.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum RestaurantErrorCode implements ErrorCodeIfs {

    RESTAURANT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), 2000, "성공");
    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
