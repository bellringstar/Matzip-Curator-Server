package com.matzip.api.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewFilterCriteria {
    private Long restaurantId;
    private Double price;
    private Double taste;
    private Double atmosphere;
    private Double portion;
    private Double noiseLevel;
    private Double service;
}
