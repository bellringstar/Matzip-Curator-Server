package com.matzip.api.domain.recommendation.event;

import com.matzip.api.common.event.DomainEvent;
import com.matzip.api.domain.recommendation.entity.UserPreference;
import lombok.Value;

@Value
public class UserPreferenceUpdatedEvent implements DomainEvent {

    Long userId;
    String username;
    UserPreference userPreference;

    @Override
    public String getEventType() {
        return "USER_PREFERENCE_UPDATED";
    }
}
