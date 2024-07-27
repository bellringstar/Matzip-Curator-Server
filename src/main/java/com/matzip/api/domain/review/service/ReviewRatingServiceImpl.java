package com.matzip.api.domain.review.service;

import com.matzip.api.common.error.ReviewErrorCode;
import com.matzip.api.common.exception.ApiException;
import com.matzip.api.common.util.ValidationUtils;
import com.matzip.api.domain.review.dto.ReviewRatingRequestDto;
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
    public ReviewRatingRequestDto addReviewRating(ReviewRatingRequestDto reviewRatingRequestDto) {
        Review review = reviewRepository.findById(reviewRatingRequestDto.getReviewId())
                .orElseThrow(() -> new ApiException(ReviewErrorCode.NOT_FOUND));
        ReviewRating reviewRating = ReviewRating.createReviewRating(review, reviewRatingRequestDto.getAspect(),
                new Rating(reviewRatingRequestDto.getRating()));
        validator.validate(reviewRating);
        ReviewRating savedReviewRating = reviewRatingRepository.save(reviewRating);
        return ReviewRatingRequestDto.toDto(savedReviewRating);
    }
}
