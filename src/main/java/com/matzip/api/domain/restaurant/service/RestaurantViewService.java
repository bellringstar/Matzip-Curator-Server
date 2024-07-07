package com.matzip.api.domain.restaurant.service;

import com.matzip.api.domain.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantViewService {

    Page<Restaurant> searchRestaurants(double latitude, double longitude, String query, int radius, Pageable pageable);

    Restaurant getRestaurantById(Long id);

    Restaurant getRestaurantByExternalId(String externalId);
}
