package com.matzip.api.domain.review.dto;

import com.matzip.api.domain.recommendation.enums.RestaurantAspect;
import java.util.Map;
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
public class ReviewFilterRequestDto {
    private Long restaurantId;
    private Map<RestaurantAspect, Double> aspectRatings;
}
