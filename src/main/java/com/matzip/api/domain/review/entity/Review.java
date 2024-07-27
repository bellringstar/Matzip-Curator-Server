package com.matzip.api.domain.review.entity;

import com.matzip.api.common.entity.BaseTimeEntity;
import com.matzip.api.domain.recommendation.enums.RestaurantAspect;
import com.matzip.api.domain.review.entity.vo.OverallRating;
import com.matzip.api.domain.review.entity.vo.Rating;
import com.matzip.api.domain.review.entity.vo.ReviewAuthor;
import com.matzip.api.domain.review.entity.vo.ReviewContent;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "reviews")
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ReviewAuthor author;

    @Column(nullable = false)
    private Long restaurantId;

    @Embedded
    private OverallRating overallRating;

    @Embedded
    private ReviewContent content;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewRating> ratings = new ArrayList<>();

    private Review(ReviewAuthor author, Long restaurantId, ReviewContent content) {
        this.author = Objects.requireNonNull(author);
        this.restaurantId = Objects.requireNonNull(restaurantId);
        this.content = Objects.requireNonNull(content);
        this.overallRating = new OverallRating(0); // 초기값
    }

    public static Review createReview(ReviewAuthor author, Long restaurantId, ReviewContent content) {
        return new Review(author, restaurantId, content);
    }

    //TODO: 동시성 이슈 생각
    public void updateContent(ReviewContent content) {
        this.content = Objects.requireNonNull(content);
    }

    public void addRating(RestaurantAspect aspect, double score) {
        ReviewRating reviewRating = ReviewRating.createReviewRating(this, aspect, new Rating(score));
        this.ratings.add(reviewRating);
        recalculateOverallRating();
    }

    public void updateRating(RestaurantAspect aspect, double score) {
        ratings.stream()
                .filter(r -> r.getAspect() == aspect)
                .findFirst()
                .ifPresent(r -> r.updateScore(score));
        recalculateOverallRating();
    }

    public void removeRating(RestaurantAspect aspect) {
        ratings.removeIf(r -> r.getAspect() == aspect);
        recalculateOverallRating();
    }

    private void recalculateOverallRating() {
        if (ratings.isEmpty()) {
            this.overallRating = new OverallRating(0);
            return;
        }

        double average = ratings.stream()
                .mapToDouble(reviewRating -> reviewRating.getRating().getScore())
                .average()
                .orElse(0.0);
        this.overallRating = new OverallRating(average);
    }

    public List<ReviewRating> getRatings() {
        return Collections.unmodifiableList(ratings);
    }
}
