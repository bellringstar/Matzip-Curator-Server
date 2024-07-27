package com.matzip.api.domain.review.service;

import com.matzip.api.common.error.ReviewErrorCode;
import com.matzip.api.common.exception.ApiException;
import com.matzip.api.common.util.ValidationUtils;
import com.matzip.api.domain.review.dto.ReviewRatingDto;
import com.matzip.api.domain.review.entity.Review;
import com.matzip.api.domain.review.entity.ReviewRating;
import com.matzip.api.domain.review.entity.vo.Rating;
import com.matzip.api.domain.review.repository.ReviewRatingRepository;
import com.matzip.api.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewRatingServiceImpl implements ReviewRatingService {

    private final ReviewRepository reviewRepository;
    private final ReviewRatingRepository reviewRatingRepository;
    private final ValidationUtils validator;

    @Override
    public ReviewRatingDto addReviewRating(ReviewRatingDto reviewRatingDto) {
        Review review = reviewRepository.findById(reviewRatingDto.getReviewId())
                .orElseThrow(() -> new ApiException(ReviewErrorCode.NOT_FOUND));
        ReviewRating reviewRating = ReviewRating.createReviewRating(review, reviewRatingDto.getAspect(),
                new Rating(reviewRatingDto.getRating()));
        validator.validate(reviewRating);
        ReviewRating savedReviewRating = reviewRatingRepository.save(reviewRating);
        return ReviewRatingDto.toDto(savedReviewRating);
    }
}