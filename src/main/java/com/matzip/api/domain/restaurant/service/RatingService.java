package com.matzip.api.domain.restaurant.service;

import com.matzip.api.domain.restaurant.entity.Restaurant;

public interface RatingService {

    double calculateRating(String userId, Restaurant restaurant);
}
