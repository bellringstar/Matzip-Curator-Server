package com.matzip.api.domain.review.repository;

import com.matzip.api.domain.review.entity.ReviewRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRatingRepository extends JpaRepository<ReviewRating, Long> {
}
