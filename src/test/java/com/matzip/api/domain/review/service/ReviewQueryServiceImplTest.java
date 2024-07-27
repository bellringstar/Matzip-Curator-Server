package com.matzip.api.domain.review.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.matzip.api.common.exception.ApiException;
import com.matzip.api.domain.review.dto.ReviewDto;
import com.matzip.api.domain.review.entity.Review;
import com.matzip.api.domain.review.entity.vo.ReviewAuthor;
import com.matzip.api.domain.review.entity.vo.ReviewContent;
import com.matzip.api.domain.review.repository.ReviewRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DisplayName("RestaurantQueryService 구현 테스트")
public class ReviewQueryServiceImplTest {

    @InjectMocks
    ReviewQueryServiceImpl reviewQueryService;

    @Mock
    ReviewRepository reviewRepository;

    Review review;

    @BeforeEach
    void setUp() {
        review = Review.createReview(new ReviewAuthor(1L,  "name"), 1L, new ReviewContent("contentcontent"));
    }

    @Test
    @DisplayName("reviewId를 통해 ReviewDto를 찾고 id를 가져올 수 있다.")
    public void test_retrieve_review_by_id_success() {

        Long reviewId = 1L;
        Review mockReview = Mockito.mock(Review.class);
        when(mockReview.getId()).thenReturn(reviewId);
        when(reviewRepository.findWithRatingsById(reviewId)).thenReturn(Optional.of(mockReview));

        // When
        ReviewDto result = reviewQueryService.getReviewById(reviewId);

        // Then
        assertNotNull(result);
        assertEquals(reviewId, result.getId());
    }

    @Test
    @DisplayName("레스토랑Id를 통해 해당 레스토랑의 리뷰를 페이징으로 가져올 수 있다.")
    public void test_retrieve_paginated_reviews_for_restaurant() {
        // Given
        Long restaurantId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> reviews = new PageImpl<>(List.of(review));
        when(reviewRepository.findWithRatingsByRestaurantId(restaurantId, pageable)).thenReturn(reviews);

        // When
        Page<ReviewDto> result = reviewQueryService.getReviewsByRestaurantId(restaurantId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("레스토랑Id를 통해 모든 리뷰를 리스트로 가져올 수 있다.")
    public void test_retrieve_all_reviews_for_restaurant() {
        // Given
        Long restaurantId = 1L;
        List<Review> reviews = List.of(review);
        when(reviewRepository.findWithRatingsByRestaurantId(restaurantId)).thenReturn(reviews);

        // When
        List<ReviewDto> result = reviewQueryService.getReviewsByRestaurantId(restaurantId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("사용자Id를 통해 해당 사용자가 작성한 모든 리뷰를 페이징으로 가져올 수 있다.")
    public void test_retrieve_paginated_reviews_by_user() {
        // Given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> reviews = new PageImpl<>(List.of(review));
        when(reviewRepository.findWithRatingsByAuthorId(userId, pageable)).thenReturn(reviews);

        // When
        Page<ReviewDto> result = reviewQueryService.getReviewsByUserId(userId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("리뷰를 최신순으로 페이징 해 가져올 수 있다.")
    public void test_retrieve_latest_reviews_paginated() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> reviews = new PageImpl<>(List.of(review));
        when(reviewRepository.findAllWithRatingsByOrderByCreatedDateDesc(pageable)).thenReturn(reviews);

        // When
        Page<ReviewDto> result = reviewQueryService.getLatestReviews(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("존재하지 않는 리뷰Id를 통해 조회하면 예외가 발생한다.")
    public void test_retrieve_review_by_non_existent_id() {
        // Given
        Long reviewId = 999L;
        when(reviewRepository.findWithRatingsById(reviewId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ApiException.class, () -> reviewQueryService.getReviewById(reviewId));
    }

    @Test
    @DisplayName("존재하지 않는 레스토랑Id를 통해 조회를 하면 빈 페이지가 반환된다.")
    public void test_retrieve_reviews_for_non_existent_restaurant() {
        // Given
        Long restaurantId = 999L;
        Pageable pageable = PageRequest.of(0, 10);
        when(reviewRepository.findWithRatingsByRestaurantId(restaurantId, pageable)).thenReturn(Page.empty());

        // When
        Page<ReviewDto> result = reviewQueryService.getReviewsByRestaurantId(restaurantId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("존재하지 않는 사용자Id로 리뷰를 조회하면 빈 페이지가 반환된다.")
    public void test_retrieve_reviews_by_non_existent_user() {
        // Given
        Long userId = 999L;
        Pageable pageable = PageRequest.of(0, 10);
        when(reviewRepository.findWithRatingsByAuthorId(userId, pageable)).thenReturn(Page.empty());

        // When
        Page<ReviewDto> result = reviewQueryService.getReviewsByUserId(userId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("존재하지 않는 레스토랑Id로 평균 평점을 계산하려 하면 예외가 발생한다.")
    public void test_calculate_average_rating_for_non_existent_restaurant() {
        // Given
        Long restaurantId = 999L;
        when(reviewRepository.findAverageRatingByRestaurantId(restaurantId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ApiException.class, () -> reviewQueryService.getAverageRatingForRestaurant(restaurantId));
    }

    @Test
    @DisplayName("리뷰가 존재하지 않는 가게의 리뷰를 조회시 비어있는 리스트가 반환된다.")
    public void test_handle_empty_review_lists_gracefully() {
        // Given
        Long restaurantId = 1L;
        when(reviewRepository.findWithRatingsByRestaurantId(restaurantId)).thenReturn(Collections.emptyList());

        // When
        List<ReviewDto> result = reviewQueryService.getReviewsByRestaurantId(restaurantId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}