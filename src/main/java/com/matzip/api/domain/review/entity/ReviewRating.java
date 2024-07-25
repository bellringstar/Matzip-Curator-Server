package com.matzip.api.domain.review.entity;

import com.matzip.api.domain.recommendation.enums.RestaurantAspect;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "review_ratings")
public class ReviewRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RestaurantAspect aspect;

    @Column(nullable = false)
    private Double rating; //Todo: VO로 변경

    public static ReviewRating createReviewRating(Review review, RestaurantAspect aspect, Double rating) {
        ReviewRating reviewRating = new ReviewRating();
        reviewRating.review = review;
        reviewRating.aspect = aspect;
        reviewRating.rating = rating;

        return reviewRating;
    }

    public void updateScore(double score) {
        rating = score;
    }
}
