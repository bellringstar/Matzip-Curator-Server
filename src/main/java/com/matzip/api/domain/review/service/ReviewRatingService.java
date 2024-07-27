package com.matzip.api.domain.review.service;

import com.matzip.api.domain.review.dto.ReviewRatingRequestDto;
import com.matzip.api.domain.review.dto.ReviewRatingResponseDto;

/**
 * The interface Review rating service.
 */
public interface ReviewRatingService {

    /**
     * Add review rating review dto.
     *
     * @param reviewRatingRequestDto the review rating dto
     * @return the review dto
     */
    ReviewRatingResponseDto addReviewRating(ReviewRatingRequestDto reviewRatingRequestDto);
}
