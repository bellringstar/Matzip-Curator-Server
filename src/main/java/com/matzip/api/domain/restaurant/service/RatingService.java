package com.matzip.api.domain.restaurant.service;

import com.matzip.api.domain.recommendation.event.UserPreferenceUpdatedEvent;
import com.matzip.api.domain.restaurant.entity.Restaurant;

public interface RatingService {

    void handleUserPreferenceUpdated(UserPreferenceUpdatedEvent event);

    double calculateRating(String userId, Restaurant restaurant);
}
