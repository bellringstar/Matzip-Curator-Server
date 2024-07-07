package com.matzip.api.common.api;

import com.matzip.api.common.error.ErrorCode;
import com.matzip.api.common.error.ErrorCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class Result {

    private final Integer resultCode;
    private final String resultMessage;
    private final Object resultDescription;

    public static Result OK() {
        return Result.builder()
                .resultCode(ErrorCode.OK.getErrorCode())
                .resultMessage(ErrorCode.OK.getDescription())
                .resultDescription(ErrorCode.OK.getDescription())
                .build();
    }

    public static Result ERROR(ErrorCodeIfs errorCode) {
        return Result.builder()
                .resultCode(errorCode.getErrorCode())
                .resultMessage(errorCode.getDescription())
                .resultDescription(errorCode.getDescription())
                .build();
    }

    public static Result ERROR(ErrorCodeIfs errorCode, Throwable tx) {
        return Result.builder()
                .resultCode(errorCode.getErrorCode())
                .resultMessage(errorCode.getDescription())
                .resultDescription(tx.getLocalizedMessage())
                .build();
    }

    /**
     * 복잡한 결과 설명을 위한 ERROR 생성 메서드
     * @param errorCode 에러 코드
     * @param description 추가적인 설명이나 데이터 (Map, List, Custom Object 등)
     * @return Result 객체
     */
    public static Result ERROR(ErrorCodeIfs errorCode, Object description) {
        return Result.builder()
                .resultCode(errorCode.getErrorCode())
                .resultMessage(errorCode.getDescription())
                .resultDescription(description)
                .build();
    }
}
