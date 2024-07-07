package com.matzip.api.domain.recommendation.service;

import com.matzip.api.domain.restaurant.entity.Restaurant;
import java.util.List;

public interface RecommendationService {
    /**
     * 사용자 위치 기반으로 추천 레스토랑 목록을 반환합니다.
     *
     * @param userId 사용자 ID
     * @param latitude 현재 위치의 위도
     * @param longitude 현재 위치의 경도
     * @param limit 반환할 최대 레스토랑 수
     * @return 추천된 레스토랑 목록
     */
    List<Restaurant> getRecommendedRestaurants(Long userId, double latitude, double longitude, int limit);

    /**
     * 특정 사용자에 대한 레스토랑의 추천 점수를 계산합니다.
     *
     * @param userId 사용자 ID
     * @param restaurantId 레스토랑 ID
     * @return 계산된 추천 점수
     */
    double calculateUserRestaurantScore(Long userId, Long restaurantId);

    /**
     * 사용자의 선호도를 업데이트합니다.
     *
     * @param userId 사용자 ID
     * @param restaurantId 레스토랑 ID
     * @param rating 사용자가 준 평점
     */
    void updateUserPreferences(Long userId, Long restaurantId, int rating);
}