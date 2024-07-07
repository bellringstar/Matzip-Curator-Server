package com.matzip.api.domain.recommendation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RestaurantAspect {
    PRICE("가격", "저렴해요", "비싸요"),
    TASTE("맛", "맛있어요", "별로에요"),
    ATMOSPHERE("분위기", "좋아요", "나빠요"),
    PORTION("양", "많아요",  "적어요"),
    NOISE_LEVEL("소음", "조용해요", "시끄러워요"),
    SERVICE("서비스", "친절해요", "불친절해요");


    private final String description;
    private final String positiveLabel;
    private final String neutralLabel = "적당해요";
    private final String negativeLabel;
}
