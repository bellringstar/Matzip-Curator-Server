package com.matzip.api.common.util;

import com.matzip.api.common.error.entity.EntityErrorCode;
import com.matzip.api.common.exception.ApiException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidationUtils {

    private final Validator validator;

    public void validate(Object entity) {
        Set<ConstraintViolation<Object>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            for (ConstraintViolation<Object> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            log.error("entity validation 에러 : {}", sb);
            throw new ApiException(EntityErrorCode.VALIDATION_FAIL, sb.toString());
        }
    }
}
