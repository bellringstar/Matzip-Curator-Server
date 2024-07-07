package com.matzip.api.domain.review.service;

import com.matzip.api.domain.review.dto.ReviewRequestDto;
import com.matzip.api.domain.review.dto.ReviewUpdateRequestDto;
import com.matzip.api.domain.review.entity.Review;

public interface ReviewService {
    /**
     * 새로운 리뷰를 생성합니다.
     *
     * @param userId 리뷰를 작성하는 사용자의 ID
     * @param restaurantId 리뷰 대상 레스토랑의 ID
     * @param request 리뷰 생성 요청 정보
     * @return 생성된 리뷰 정보
     */
    Review createReview(Long userId, Long restaurantId, ReviewRequestDto request);

    /**
     * 기존 리뷰를 수정합니다.
     *
     * @param id 수정할 리뷰의 ID
     * @param request 리뷰 수정 요청 정보
     * @return 수정된 리뷰 정보
     */
    Review updateReview(Long id, ReviewUpdateRequestDto request);

    /**
     * 리뷰를 삭제합니다.
     *
     * @param id 삭제할 리뷰의 ID
     */
    void deleteReview(Long id);
}
