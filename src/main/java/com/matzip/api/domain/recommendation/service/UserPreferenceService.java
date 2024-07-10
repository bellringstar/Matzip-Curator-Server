package com.matzip.api.domain.recommendation.service;

import com.matzip.api.domain.recommendation.entity.UserPreference;

public interface UserPreferenceService {

    UserPreference getUserPreference(String userId);
}
