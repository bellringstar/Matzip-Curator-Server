package com.matzip.api.common.exceptionhandler;

import com.matzip.api.common.api.Api;
import com.matzip.api.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Api<Object>> handleApiException(ApiException exception) {
        Api<Object> errorResponse = Api.ERROR(exception.getErrorCodeIfs(), exception.getErrorDescription());

        log.error("{}", errorResponse);

        HttpStatus httpStatus = HttpStatus.valueOf(exception.getErrorCodeIfs().getHttpStatusCode());

        return ResponseEntity
                .status(httpStatus)
                .body(errorResponse);
    }
}
