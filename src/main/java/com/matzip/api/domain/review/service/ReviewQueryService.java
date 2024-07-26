package com.matzip.api.domain.review.service;

import com.matzip.api.domain.review.dto.ReviewDto;
import com.matzip.api.domain.review.entity.Review;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Review query service.
 */
public interface ReviewQueryService {
    /**
     * ID로 특정 리뷰를 조회합니다.
     *
     * @param id 조회할 리뷰의 ID
     * @return 조회된 리뷰 정보
     */
    ReviewDto getReviewById(Long id);

    /**
     * 특정 레스토랑의 리뷰 목록을 조회합니다.
     *
     * @param restaurantId 레스토랑 ID
     * @param pageable     페이징 정보
     * @return 해당 레스토랑의 리뷰 목록 (페이지)
     */
    Page<ReviewDto> getReviewsByRestaurantId(Long restaurantId, Pageable pageable);


    /**
     * 특정 레스토랑의 모든 리뷰를 조회합니다.
     *
     * @param restaurantId the restaurant id
     * @return the reviews by restaurant id
     */
    List<ReviewDto> getReviewsByRestaurantId(Long restaurantId);

    /**
     * 특정 사용자가 작성한 리뷰 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 해당 사용자의 리뷰 목록 (페이지)
     */
    Page<ReviewDto> getReviewsByUserId(Long userId, Pageable pageable);

    /**
     * 최신 리뷰 목록을 조회합니다.
     *
     * @param pageable 페이징 정보
     * @return 최신 리뷰 목록 (페이지)
     */
    Page<ReviewDto> getLatestReviews(Pageable pageable);

    /**
     * 해당 레스토랑의 리뷰 평균 평점을 계산해 반환합니다.
     *
     * @param restaurantId 레스토랑 ID
     * @return 리뷰 종합 평균 평점
     */
    double getAverageRatingForRestaurant(Long restaurantId);
}