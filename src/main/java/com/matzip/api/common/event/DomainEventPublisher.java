package com.matzip.api.common.event;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
