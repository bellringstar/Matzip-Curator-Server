package com.matzip.api.domain.recommendation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RestaurantAspect {
    PRICE("가격"),
    TASTE("맛"),
    ATMOSPHERE("분위기"),
    PORTION("양"),
    NOISE_LEVEL("소음"),
    SERVICE("서비스");


    private final String description;
}
