package com.matzip.api.domain.recommendation.event;

import com.matzip.api.common.event.DomainEvent;
import lombok.Value;

@Value
public class UserPreferenceRequestEvent implements DomainEvent {

    String username;

    @Override
    public String getEventType() {
        return "USER_PREFERENCE_REQUEST";
    }
}
