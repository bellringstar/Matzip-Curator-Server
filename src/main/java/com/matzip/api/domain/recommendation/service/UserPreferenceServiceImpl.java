package com.matzip.api.domain.recommendation.service;

import com.matzip.api.common.error.UserPreferenceErrorCode;
import com.matzip.api.common.event.DomainEventPublisher;
import com.matzip.api.common.exception.ApiException;
import com.matzip.api.domain.recommendation.entity.AspectPreference;
import com.matzip.api.domain.recommendation.entity.UserPreference;
import com.matzip.api.domain.recommendation.enums.RestaurantAspect;
import com.matzip.api.domain.recommendation.event.UserPreferenceUpdatedEvent;
import com.matzip.api.domain.recommendation.repository.UserPreferenceRepository;
import com.matzip.api.domain.user.event.UserCreatedEvent;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPreferenceServiceImpl implements
        UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final DomainEventPublisher eventPublisher;

    @EventListener
    @Override
    public void handleUserCreated(UserCreatedEvent event) {
        UserPreference preference = UserPreference.createForUser(event.getUser());
        userPreferenceRepository.save(preference);
    }

    @Override
    public void updateUserPreference(Long userId, Map<RestaurantAspect, Double> newScores) {
        UserPreference userPreference = userPreferenceRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserPreferenceErrorCode.USER_PREFERENCE_NOT_FOUND));

        newScores.forEach(((aspect, score) -> {
            userPreference.updateAspectPreference(aspect, score);
            AspectPreference updatedAspect = userPreference.getAspectPreferences().stream()
                    .filter(ap -> ap.getAspect() == aspect)
                    .findFirst()
                    .orElseThrow();

        }));

        userPreferenceRepository.save(userPreference);

        eventPublisher.publish(new UserPreferenceUpdatedEvent(
                userPreference.getId(),
                userPreference.getUser().getUsername(),
                userPreference
        ));
    }

    @Override
    public UserPreference getUserPreference(String userId) {
        return null;
    }
}
