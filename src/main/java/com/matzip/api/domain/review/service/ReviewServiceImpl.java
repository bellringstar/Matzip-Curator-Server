package com.matzip.api.domain.review.service;

import com.matzip.api.common.error.ReviewErrorCode;
import com.matzip.api.common.event.DomainEventPublisher;
import com.matzip.api.common.exception.ApiException;
import com.matzip.api.common.util.ValidationUtils;
import com.matzip.api.domain.review.client.RestaurantClient;
import com.matzip.api.domain.review.dto.ReviewDto;
import com.matzip.api.domain.review.dto.ReviewRatingRequestDto;
import com.matzip.api.domain.review.dto.ReviewRequestDto;
import com.matzip.api.domain.review.entity.Review;
import com.matzip.api.domain.review.entity.vo.ReviewAuthor;
import com.matzip.api.domain.review.entity.vo.ReviewContent;
import com.matzip.api.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewRatingService reviewRatingService;
    private final ValidationUtils validator;
    private final RestaurantClient restaurantClient;

    @Override
    public ReviewDto createReview(ReviewRequestDto request, ReviewAuthor author) {
        boolean validRestaurant = restaurantClient.isValidRestaurant(request.getRestaurantId());
        if (!validRestaurant) {
            throw new ApiException(ReviewErrorCode.NOT_FOUND, "restaurant not found");
        }
        Review review = Review.createReview(author, request.getRestaurantId(), new ReviewContent(request.getContent()));
        validator.validate(review);
        Review savedReview = reviewRepository.save(review);
        for (ReviewRatingRequestDto rating : request.getRatings()) {
            rating.setReviewId(savedReview.getId());
            reviewRatingService.addReviewRating(rating);
        }
        return ReviewDto.toDto(savedReview);
    }

    @Override
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ApiException(ReviewErrorCode.NOT_FOUND));
        review.deleteReview();
    }
}
