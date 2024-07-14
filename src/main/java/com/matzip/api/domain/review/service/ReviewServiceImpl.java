package com.matzip.api.domain.review.service;

import com.matzip.api.domain.review.dto.ReviewRequestDto;
import com.matzip.api.domain.review.dto.ReviewUpdateRequestDto;
import com.matzip.api.domain.review.entity.Review;
import com.matzip.api.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Review createReview(Long userId, Long restaurantId, ReviewRequestDto request) {
        return null;
    }

    @Override
    public Review updateReview(Long id, ReviewUpdateRequestDto request) {
        return null;
    }

    @Override
    public void deleteReview(Long id) {

    }
}
