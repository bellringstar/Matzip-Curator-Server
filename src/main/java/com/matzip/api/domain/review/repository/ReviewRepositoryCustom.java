package com.matzip.api.domain.review.repository;

import com.matzip.api.domain.review.dto.ReviewDto;
import com.matzip.api.domain.review.dto.ReviewFilterRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    Page<ReviewDto> findFilteredReviews(ReviewFilterRequestDto filterRequest, Pageable pageable);
}
