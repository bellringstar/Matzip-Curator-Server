package com.matzip.api.domain.recommendation.dto;

import com.matzip.api.domain.recommendation.enums.RestaurantAspect;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AspectPreferenceData {
    RestaurantAspect aspect;
    Double score;
    Double weight;
}
