package com.matzip.api.domain.review.service;

import com.matzip.api.domain.review.entity.Review;
import com.matzip.api.domain.review.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewQueryServiceImpl implements ReviewQueryService {

    private final ReviewRepository reviewRepository;

    @Override
    public Review getReviewById(Long id) {
        return null;
    }

    @Override
    public Page<Review> getReviewsByRestaurantId(Long restaurantId, Pageable pageable) {
        return null;
    }

    @Override
    public List<Review> getReviewsForRestaurant(Long restaurantId) {
        return List.of();
    }

    @Override
    public Page<Review> getReviewsByUserId(Long userId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Review> getLatestReviews(Pageable pageable) {
        return null;
    }
}
