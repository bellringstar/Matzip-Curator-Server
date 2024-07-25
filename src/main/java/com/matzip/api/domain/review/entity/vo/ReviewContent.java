package com.matzip.api.domain.review.entity.vo;

import com.matzip.api.common.error.ReviewErrorCode;
import com.matzip.api.common.exception.ApiException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class ReviewContent {

    @Column(nullable = false)
    @Size(min = 10, max = 1000)
    private final String value;

    protected ReviewContent() {
        this.value = null;
    }

    public ReviewContent(String value) {
        validateContent(value);
        this.value = value;
    }

    public ReviewContent updateContent(String newValue) {
        validateContent(newValue);
        return new ReviewContent(newValue);
    }

    private void validateContent(String value) {
        if (value == null || value.trim().length() < 10 || value.length() > 1000) {
            throw new ApiException(ReviewErrorCode.INVALID_CONTENT);
        }
    }
}
