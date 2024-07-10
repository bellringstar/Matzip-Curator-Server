package com.matzip.api.domain.restaurant.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class LocationSearchRequest {

    public static final int DEFAULT_RADIUS = 500;

    private final double latitude;
    private final double longitude;
    private final String query;
    private final int radius;

    public static LocationSearchRequest of(double latitude, double longitude, String query) {
        return new LocationSearchRequest(latitude, longitude, query, DEFAULT_RADIUS);
    }

    public static LocationSearchRequest of(double latitude, double longitude, String query, int radius) {
        return new LocationSearchRequest(latitude, longitude, query, radius);
    }
}
