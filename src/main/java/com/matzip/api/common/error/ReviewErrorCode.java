package com.matzip.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ReviewErrorCode implements ErrorCodeIfs {

    INVALID_RATING(HttpStatus.BAD_REQUEST.value(), 9000, "overall rating은 1~5 사이의 값만 가능합니다."),
    INVALID_CONTENT(HttpStatus.BAD_REQUEST.value(), 9001, "content의 길이는 10~1000 사이만 가능합니다."),
    NOT_FOUND(HttpStatus.BAD_REQUEST.value(), 9002, "존재하지 않는 리뷰입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST.value(), 9003, "리뷰를 생성할 수 없습니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
