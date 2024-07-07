package com.matzip.api.common.exceptionhandler;

import com.matzip.api.common.api.Api;
import com.matzip.api.common.error.ErrorCode;
import com.matzip.api.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Api<Object>> handleApiException(ApiException exception) {
        Api<Object> errorResponse = Api.ERROR(ErrorCode.SERVER_ERROR, exception.getErrorDescription());

        log.error("{}", errorResponse);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Api<Object>> handleBindException(BindException exception) {
        String errorMessage = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        Api<Object> errorResponse = Api.ERROR(ErrorCode.BAD_REQUEST, errorMessage);

        log.error("{}", errorResponse);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
