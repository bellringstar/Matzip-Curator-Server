package com.matzip.api.domain.review.service;

import com.matzip.api.domain.review.dto.ReviewRatingDto;

/**
 * The interface Review rating service.
 */
public interface ReviewRatingService {

    /**
     * Add review rating review dto.
     *
     * @param reviewRatingDto the review rating dto
     * @return the review dto
     */
    ReviewRatingDto addReviewRating(ReviewRatingDto reviewRatingDto);
}
