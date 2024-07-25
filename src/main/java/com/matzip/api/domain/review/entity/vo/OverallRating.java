package com.matzip.api.domain.review.entity.vo;

import com.matzip.api.common.error.ErrorCode;
import com.matzip.api.common.error.ReviewErrorCode;
import com.matzip.api.common.exception.ApiException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class OverallRating {

    @Column(nullable = false)
    private final double value;

    protected OverallRating() {
        value = 0.0;
    }

    public OverallRating(double value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(double value) {
        if (value < 0 || value > 5) {
            throw new ApiException(ReviewErrorCode.INVALID_OVERALL_RATING);
        }
    }
}
