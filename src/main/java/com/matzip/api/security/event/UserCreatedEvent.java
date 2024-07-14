package com.matzip.api.security.event;

import com.matzip.api.common.event.DomainEvent;
import com.matzip.api.domain.user.entity.User;
import lombok.Value;

@Value
public class UserCreatedEvent implements DomainEvent {

    User user;

    @Override
    public String getEventType() {
        return "USER_CREATED";
    }
}
