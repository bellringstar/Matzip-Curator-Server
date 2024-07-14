package com.matzip.api.domain.recommendation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.matzip.api.common.event.DomainEventPublisher;
import com.matzip.api.common.exception.ApiException;
import com.matzip.api.domain.recommendation.entity.UserPreference;
import com.matzip.api.domain.recommendation.enums.RestaurantAspect;
import com.matzip.api.domain.recommendation.event.UserPreferenceUpdatedEvent;
import com.matzip.api.domain.recommendation.repository.UserPreferenceRepository;
import com.matzip.api.domain.user.entity.User;
import com.matzip.api.security.dto.UserCreateRequestDto;
import com.matzip.api.security.event.UserCreatedEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserPreferenceServiceImplTest {

    @Mock
    private UserPreferenceRepository userPreferenceRepository;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private UserPreferenceServiceImpl userPreferenceService;

    private User user;
    private UserPreference userPreference;

    @BeforeEach
    void setUp() {
        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto();
        userCreateRequestDto.setLoginId("testUser");
        userCreateRequestDto.setPassword("password");
        userCreateRequestDto.setEmail("test@example.com");
        user = User.createUser(userCreateRequestDto);
        userPreference = UserPreference.createForUser(user);
    }

    @Nested
    @DisplayName("handleUserCreated 메서드는")
    class HandleUserCreatedTest {

        @Test
        @DisplayName("새 사용자에 대한 UserPreference를 생성하고 저장한다")
        void createsAndSavesUserPreferenceForNewUser() {
            UserCreatedEvent event = new UserCreatedEvent(user);

            userPreferenceService.handleUserCreated(event);

            verify(userPreferenceRepository).save(any(UserPreference.class));
        }
    }

    @Nested
    @DisplayName("updateUserPreference 메서드는")
    class UpdateUserPreferenceTest {

        @Test
        @DisplayName("존재하는 UserPreference를 업데이트하고 이벤트를 발행한다")
        void updatesExistingUserPreferenceAndPublishesEvent() {
            Long userId = 1L;
            Map<RestaurantAspect, Double> newScores = new HashMap<>();
            newScores.put(RestaurantAspect.TASTE, 4.5);
            newScores.put(RestaurantAspect.ATMOSPHERE, 3.5);

            when(userPreferenceRepository.findById(userId)).thenReturn(Optional.of(userPreference));
            userPreferenceService.updateUserPreference(userId, newScores);

            verify(userPreferenceRepository).save(userPreference);

            ArgumentCaptor<UserPreferenceUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(
                    UserPreferenceUpdatedEvent.class);
            verify(eventPublisher).publish(eventCaptor.capture());

            UserPreferenceUpdatedEvent capturedEvent = eventCaptor.getValue();
            assertEquals(user.getLoginId(), capturedEvent.getUserPreference().getUser().getLoginId());
            assertEquals(user.getLoginId(), capturedEvent.getUsername());
            assertSame(userPreference, capturedEvent.getUserPreference());
        }

        @Test
        @DisplayName("존재하지 않는 UserPreference에 대해 예외를 던진다")
        void throwsExceptionForNonExistentUserPreference() {
            Long userId = 1L;
            Map<RestaurantAspect, Double> newScores = new HashMap<>();

            when(userPreferenceRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(ApiException.class, () -> userPreferenceService.updateUserPreference(userId, newScores));
        }
    }

    @Nested
    @DisplayName("getUserPreference 메서드는")
    class GetUserPreferenceTest {

        @Test
        @DisplayName("현재 구현에서는 null을 반환한다")
        void returnsNullInCurrentImplementation() {
            String userId = "testUser";

            UserPreference result = userPreferenceService.getUserPreference(userId);

            assertNull(result);
        }
    }
}