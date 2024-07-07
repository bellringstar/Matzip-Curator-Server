package com.matzip.api.domain.review.entity;

import com.matzip.api.common.entity.BaseTimeEntity;
import com.matzip.api.domain.restaurant.entity.Restaurant;
import com.matzip.api.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    @Min(1)
    @Min(5)
    private Integer rating;

    @Column(nullable = false)
    @Size(min = 10, max = 1000)
    private String content;


    public static Review createReview(User user, Restaurant restaurant, Integer rating, String content) {
        Review review = new Review();
        review.user = user;
        review.restaurant = restaurant;
        review.rating = rating;
        review.content = content;
        restaurant.addReview(review);
        return review;
    }
}