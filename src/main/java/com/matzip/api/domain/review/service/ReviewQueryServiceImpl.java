package com.matzip.api.domain.review.service;

import com.matzip.api.common.error.ReviewErrorCode;
import com.matzip.api.common.exception.ApiException;
import com.matzip.api.domain.review.dto.ReviewDto;
import com.matzip.api.domain.review.entity.Review;
import com.matzip.api.domain.review.repository.ReviewRepository;
import java.util.List;
import java.util.stream.Collectors;
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
                .orElseThrow(() -> new ApiException(ReviewErrorCode.NOT_FOUND));
    }

    @Override
    public Page<ReviewDto> getReviewsByRestaurantId(Long restaurantId, Pageable pageable) {
        return reviewRepository.findWithRatingsByRestaurantId(restaurantId, pageable)
                .map(ReviewDto::toDto);
    }

    @Override
    public List<ReviewDto> getReviewsByRestaurantId(Long restaurantId) {
        return reviewRepository.findWithRatingsByRestaurantId(restaurantId)
                .stream().map(ReviewDto::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Page<ReviewDto> getReviewsByUserId(Long userId, Pageable pageable) {
        return reviewRepository.findWithRatingsByAuthorId(userId, pageable)
                .map(ReviewDto::toDto);
    }

    @Override
    public Page<ReviewDto> getLatestReviews(Pageable pageable) {
        return reviewRepository.findAllWithRatingsByOrderByCreatedDateDesc(pageable)
                .map(ReviewDto::toDto);
    }

    @Override
    public double getAverageRatingForRestaurant(Long restaurantId) {
        return reviewRepository.findAverageRatingByRestaurantId(restaurantId)
                .orElseThrow(() -> new ApiException(ReviewErrorCode.NOT_FOUND));
    }
}
