package com.matzip.api.common.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@DisplayName("SpringDomainEventPublisher 테스트")
public class SpringDomainEventPublisherTest {

    private ApplicationEventPublisher applicationEventPublisher;
    private SpringDomainEventPublisher springDomainEventPublisher;

    @BeforeEach
    @DisplayName("테스트 초기화")
    public void setUp() {
        applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        springDomainEventPublisher = new SpringDomainEventPublisher(applicationEventPublisher);
    }

    @Test
    @DisplayName("이벤트 게시 테스트")
    public void testPublish() {
        // Given
        DomainEvent event = new DomainEvent() {
            @Override
            public String getEventType() {
                return "";
            }
        };

        // When
        springDomainEventPublisher.publish(event);

        // Then
        ArgumentCaptor<DomainEvent> eventCaptor = ArgumentCaptor.forClass(DomainEvent.class);
        verify(applicationEventPublisher).publishEvent(eventCaptor.capture());

        DomainEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent).isEqualTo(event);
    }
}
