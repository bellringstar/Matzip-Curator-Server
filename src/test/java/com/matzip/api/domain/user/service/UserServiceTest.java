package com.matzip.api.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.matzip.api.common.event.DomainEventPublisher;
import com.matzip.api.domain.user.dto.UserCreateRequestDto;
import com.matzip.api.domain.user.entity.User;
import com.matzip.api.domain.user.enums.AuthProvider;
import com.matzip.api.domain.user.event.UserCreatedEvent;
import com.matzip.api.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("UserService 클래스")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("createUser 메서드는")
    class Describe_createUser {

        private UserCreateRequestDto requestDto;
        private User savedUser;

        @BeforeEach
        void setUp() {
            requestDto = new UserCreateRequestDto("testuser", "test@example.com", "password", "Test User", false,
                    AuthProvider.LOCAL, null);
            savedUser = User.createUser(requestDto);
            when(userRepository.save(any(User.class))).thenReturn(savedUser);
        }

        @Test
        @DisplayName("새로운 유저를 생성하고 이벤트를 발행한다")
        void it_creates_new_user_and_publishes_event() {
            // When
            userService.createUser(requestDto);

            // Then
            verify(userRepository).save(any(User.class));

            ArgumentCaptor<UserCreatedEvent> eventCaptor = ArgumentCaptor.forClass(UserCreatedEvent.class);
            verify(eventPublisher).publish(eventCaptor.capture());

            UserCreatedEvent capturedEvent = eventCaptor.getValue();
            assertThat(capturedEvent.getUser()).isEqualTo(savedUser);
            assertThat(capturedEvent.getEventType()).isEqualTo("USER_CREATED");
        }
    }
}