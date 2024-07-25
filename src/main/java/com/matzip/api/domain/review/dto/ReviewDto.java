package com.matzip.api.domain.review.dto;

import com.matzip.api.common.entity.BaseTimeEntity;
import com.matzip.api.domain.recommendation.enums.RestaurantAspect;
import com.matzip.api.domain.review.entity.Review;
import com.matzip.api.domain.review.entity.ReviewRating;
import com.matzip.api.domain.review.entity.vo.OverallRating;
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
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ReviewDto extends BaseTimeEntity {

    private Long id;

    private ReviewAuthor author;

    private Long restaurantId;

    private OverallRating overallRating;

    private ReviewContent content;

    private List<ReviewRating> ratings = new ArrayList<>();

    private ReviewDto(ReviewAuthor author, Long restaurantId, ReviewContent content) {
        this.author = Objects.requireNonNull(author);
        this.restaurantId = Objects.requireNonNull(restaurantId);
        this.content = Objects.requireNonNull(content);
        this.overallRating = new OverallRating(0); // 초기값
    }


    public static ReviewDto toDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.id = review.getId();
        dto.author = review.getAuthor();
        dto.restaurantId = review.getRestaurantId();
        dto.overallRating = review.getOverallRating();
        dto.content = review.getContent();
        dto.ratings = review.getRatings();

        return dto;
    }

    public List<ReviewRating> getRatings() {
        return Collections.unmodifiableList(ratings);
    }
}
