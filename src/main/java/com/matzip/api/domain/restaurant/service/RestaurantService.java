package com.matzip.api.domain.restaurant.service;

import com.matzip.api.domain.restaurant.dto.RestaurantCreateRequest;
import com.matzip.api.domain.restaurant.dto.RestaurantUpdateRequest;
import com.matzip.api.domain.restaurant.entity.Restaurant;

public interface RestaurantService {

    Restaurant createRestaurant(RestaurantCreateRequest request);

    Restaurant updateRestaurant(Long id, RestaurantUpdateRequest request);

    void deleteRestaurant(Long id);

    void syncWithExternalApi();
}
