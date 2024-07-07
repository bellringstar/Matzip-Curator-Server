package com.matzip.api.common.api;

import com.matzip.api.common.error.ErrorCodeIfs;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Api<T> {

    private final Result result;

    @Valid
    private final T body;

    public static <T> Api<T> OK(T data) {
        return new Api<>(Result.OK(), data);
    }

    public static Api<Object> ERROR(ErrorCodeIfs errorCode) {
        return new Api<Object>(Result.ERROR(errorCode), Optional.empty());
    }

    public static Api<Object> ERROR(ErrorCodeIfs errorCode, Throwable tx) {
        return new Api<Object>(Result.ERROR(errorCode, tx), Optional.empty());
    }

    public static Api<Object> ERROR(ErrorCodeIfs errorCode, Object description) {
        return new Api<Object>(Result.ERROR(errorCode, description), Optional.empty());
    }
}
