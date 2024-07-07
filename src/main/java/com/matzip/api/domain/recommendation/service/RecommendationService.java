package com.matzip.api.domain.recommendation.service;

import com.matzip.api.domain.restaurant.entity.Restaurant;
import java.util.List;

public interface RecommendationService {
    List<Restaurant> getRecommendedRestaurant(Long userId, double latitude, double longitude);
    double calculateUserRestaurantScore(Long userId, Long restaurantId);
    void updateUserPreferences(Long userId, int rating);
}
