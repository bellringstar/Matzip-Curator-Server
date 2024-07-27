package com.matzip.api.domain.review.service;

import com.matzip.api.domain.review.dto.ReviewRatingRequestDto;

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
    ReviewRatingRequestDto addReviewRating(ReviewRatingRequestDto reviewRatingRequestDto);
}
