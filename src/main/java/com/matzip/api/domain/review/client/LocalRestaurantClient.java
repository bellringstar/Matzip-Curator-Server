package com.matzip.api.domain.review.client;

import com.matzip.api.domain.restaurant.repository.RestaurantRepository;
import com.matzip.api.domain.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalRestaurantClient implements RestaurantClient {

    private final RestaurantRepository restaurantRepository;

    @Override
    public boolean isValidRestaurant(Long restaurantId) {
        return restaurantRepository.existsById(restaurantId);
    }
}
