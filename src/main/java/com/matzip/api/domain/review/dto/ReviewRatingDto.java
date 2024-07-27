package com.matzip.api.domain.review.dto;

import com.matzip.api.domain.recommendation.enums.RestaurantAspect;
import com.matzip.api.domain.review.entity.Review;
import com.matzip.api.domain.review.entity.ReviewRating;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ReviewRatingDto {

    // null인 경우 새로 생성되는 리뷰에 대한 레이팅 null이 아닌 경우 기존의 리뷰에 추가하는 점수
    private Long reviewId;

    @NotNull
    private RestaurantAspect aspect;

    @NotNull
    @Min(1)
    @Max(5)
    private double rating;

    public static ReviewRatingDto toDto(ReviewRating rating) {
        return ReviewRatingDto.builder()
                .reviewId(rating.getReview().getId())
                .aspect(rating.getAspect())
                .rating(rating.getRating().getScore())
                .build();
    }
}
