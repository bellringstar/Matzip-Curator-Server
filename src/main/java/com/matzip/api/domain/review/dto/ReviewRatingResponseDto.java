package com.matzip.api.domain.review.dto;

import com.matzip.api.domain.recommendation.enums.RestaurantAspect;
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
public class ReviewRatingResponseDto {

    private Long reviewId;

    @NotNull
    private RestaurantAspect aspect;

    @NotNull
    @Min(1)
    @Max(5)
    private double rating;

    public static ReviewRatingResponseDto toDto(ReviewRating rating) {
        return ReviewRatingResponseDto.builder()
                .reviewId(rating.getReview().getId())
                .aspect(rating.getAspect())
                .rating(rating.getRating().getScore())
                .build();
    }
}
