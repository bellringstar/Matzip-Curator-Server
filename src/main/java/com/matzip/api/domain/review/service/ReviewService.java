package com.matzip.api.domain.review.service;

import com.matzip.api.domain.review.dto.ReviewRequestDto;
import com.matzip.api.domain.review.dto.ReviewUpdateRequestDto;
import com.matzip.api.domain.review.entity.Review;

public interface ReviewService {
    Review createReview(Long userId, Long restaurantId, ReviewRequestDto request);

    Review updateReview(Long id, ReviewUpdateRequestDto request);

    void deleteReview(Long id);
}
