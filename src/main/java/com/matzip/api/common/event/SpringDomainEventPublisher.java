package com.matzip.api.common.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventProcessor;

    @Override
    public void publish(DomainEvent event) {
        applicationEventProcessor.publishEvent(event);
    }
}
