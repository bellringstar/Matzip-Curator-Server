package com.matzip.api.domain.review.entity.vo;

import com.matzip.api.common.error.ReviewErrorCode;
import com.matzip.api.common.exception.ApiException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class Rating {

    @Column(nullable = false)
    private final double score;

    protected Rating() {
        score = 0.0;
    }

    public Rating(double score) {
        validateValue(score);
        this.score = score;
    }

    private void validateValue(double value) {
        if (value < 0 || value > 5) {
            throw new ApiException(ReviewErrorCode.INVALID_RATING);
        }
    }
}
