package com.matzip.api.domain.review.repository;

import com.matzip.api.domain.review.entity.Review;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"ratings"})
    Optional<Review> findWithRatingsById(Long id);

    @EntityGraph(attributePaths = {"ratings"})
    Page<Review> findWithRatingsByRestaurantId(Long restaurantId, Pageable pageable);

    @EntityGraph(attributePaths = {"ratings"})
    Page<Review> findWithRatingsByAuthorId(Long authorId, Pageable pageable);

    @EntityGraph(attributePaths = {"ratings"})
    Page<Review> findAllWithRatingsByOrderByCreatedDateDesc(Pageable pageable);

    @Query("SELECT AVG(r.overallRating.value) FROM Review r WHERE r.restaurantId = :restaurantId")
    Optional<Double> findAverageRatingByRestaurantId(@Param("restaurantId") Long restaurantId);
}
