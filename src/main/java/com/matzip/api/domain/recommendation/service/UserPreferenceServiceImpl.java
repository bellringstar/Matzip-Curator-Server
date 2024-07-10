package com.matzip.api.domain.recommendation.service;

import com.matzip.api.domain.recommendation.entity.UserPreference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPreferenceServiceImpl implements
        UserPreferenceService {
    @Override
    public UserPreference getUserPreference(String userId) {
        return null;
    }
}
