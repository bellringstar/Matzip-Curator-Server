package com.matzip.api.domain.restaurant.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestaurantSearchCriteria {
    private final double latitude;
    private final double longitude;
    private final String query;
    private final int radius;
    private final String userId;
    private final String sortBy;

    public static RestaurantSearchCriteria of(double latitude, double longitude, String query, int radius, String userId, String sortBy) {
        return new RestaurantSearchCriteria(latitude, longitude, query, radius, userId, sortBy);
    }
}
