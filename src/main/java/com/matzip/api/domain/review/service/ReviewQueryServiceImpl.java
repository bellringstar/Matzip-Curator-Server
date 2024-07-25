package com.matzip.api.domain.review.service;

import com.matzip.api.domain.review.dto.ReviewDto;
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
    public ReviewDto getReviewById(Long id) {
        return reviewRepository.findWithRatingsById(id)
                .map(ReviewDto::toDto)
                .orElseThrow();
    }

    @Override
    public Page<ReviewDto> getReviewsByRestaurantId(Long restaurantId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReviewDto> getReviewsByUserId(Long userId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReviewDto> getLatestReviews(Pageable pageable) {
        return null;
    }

    @Override
    public double getAverageRatingForRestaurant(Long restaurantId) {
        return 0;
    }
}
