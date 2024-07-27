package com.matzip.api.domain.review.service;

import com.matzip.api.domain.review.dto.ReviewDto;
import com.matzip.api.domain.review.dto.ReviewRatingDto;
import com.matzip.api.domain.review.dto.ReviewRequestDto;
import com.matzip.api.domain.review.dto.ReviewUpdateRequestDto;
import com.matzip.api.domain.review.entity.Review;
import com.matzip.api.domain.review.entity.vo.ReviewAuthor;
import com.matzip.api.domain.review.entity.vo.ReviewContent;
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
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewRatingRepository reviewRatingRepository;
    private final ReviewRatingService reviewRatingService;

    @Override
    public ReviewDto createReview(ReviewRequestDto request, ReviewAuthor author) {
        Review review = Review.createReview(author, request.getRestaurantId(), new ReviewContent(request.getContent()));
        Review savedReview = reviewRepository.save(review);
        // auto_increment 를 사용중이라 save 쿼리가 나가야 id가 생긴다.
        for (ReviewRatingDto rating : request.getRatings()) {
            rating.setReviewId(savedReview.getId());
            reviewRatingService.addReviewRating(rating);
        }
        return ReviewDto.toDto(savedReview);
    }

    @Override
    public ReviewDto updateReview(Long id, ReviewUpdateRequestDto request) {
        return null;
    }

    @Override
    public void deleteReview(Long id) {

    }
}
