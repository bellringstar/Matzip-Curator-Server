package com.matzip.api.domain.review.service;

import com.matzip.api.domain.review.dto.ReviewDto;
import com.matzip.api.domain.review.dto.ReviewRequestDto;
import com.matzip.api.domain.review.dto.ReviewUpdateRequestDto;
import com.matzip.api.domain.review.entity.Review;
import com.matzip.api.domain.review.entity.vo.ReviewAuthor;
import java.util.List;

public interface ReviewService {
    /**
     * 새로운 리뷰를 생성합니다.
     *
     * @param request 리뷰 생성 요청 정보
     * @param author 리뷰를 작성자
     * @return 생성된 리뷰 정보
     */
    ReviewDto createReview(ReviewRequestDto request, ReviewAuthor author);

    /**
     * 기존 리뷰를 수정합니다.
     *
     * @param id 수정할 리뷰의 ID
     * @param request 리뷰 수정 요청 정보
     * @return 수정된 리뷰 정보
     */
    ReviewDto updateReview(Long id, ReviewUpdateRequestDto request);

    /**
     * 리뷰를 삭제합니다.
     *
     * @param id 삭제할 리뷰의 ID
     */
    void deleteReview(Long id);

}
