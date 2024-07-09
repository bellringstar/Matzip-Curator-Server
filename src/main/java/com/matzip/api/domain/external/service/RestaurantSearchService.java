package com.matzip.api.domain.external.service;

import com.matzip.api.domain.external.dto.RestaurantSearchResponse;

public interface RestaurantSearchService {
    RestaurantSearchResponse searchRestaurants(double latitude, double longitude, String query, int radius);
}
