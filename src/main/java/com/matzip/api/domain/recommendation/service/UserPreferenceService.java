package com.matzip.api.domain.recommendation.service;

import com.matzip.api.domain.recommendation.entity.UserPreference;
import com.matzip.api.domain.recommendation.enums.RestaurantAspect;
import com.matzip.api.domain.user.event.UserCreatedEvent;
import java.util.Map;

public interface UserPreferenceService {

    void handleUserCreated(UserCreatedEvent event);
    void updateUserPreference(Long userId, Map<RestaurantAspect, Double> newScores);
    UserPreference getUserPreference(String userId);
}
