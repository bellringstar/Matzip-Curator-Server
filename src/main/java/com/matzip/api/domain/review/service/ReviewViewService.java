package com.matzip.api.domain.review.service;

import com.matzip.api.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewViewService {
    Review getReviewById(Long id);

    Page<Review> getReviewsByRestaurantId(Long restaurantId, Pageable pageable);
}
