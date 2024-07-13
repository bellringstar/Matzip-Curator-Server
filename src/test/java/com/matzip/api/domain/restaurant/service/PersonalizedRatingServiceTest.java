package com.matzip.api.domain.restaurant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.matzip.api.common.event.DomainEventPublisher;
import com.matzip.api.domain.recommendation.entity.UserPreference;
import com.matzip.api.domain.recommendation.enums.RestaurantAspect;
import com.matzip.api.domain.recommendation.event.UserPreferenceRequestEvent;
import com.matzip.api.domain.recommendation.event.UserPreferenceUpdatedEvent;
import com.matzip.api.domain.restaurant.entity.Restaurant;
import com.matzip.api.domain.restaurant.entity.RestaurantCharacteristic;
import com.matzip.api.domain.review.entity.Review;
import com.matzip.api.domain.review.entity.ReviewRating;
import com.matzip.api.domain.review.service.ReviewViewService;
import com.matzip.api.domain.user.entity.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersonalizedRatingServiceTest {

    @Mock
    private ReviewViewService reviewViewService;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private PersonalizedRatingService personalizedRatingService;

    private Restaurant restaurant;
    private UserPreference userPreference;
    private List<Review> reviews;

    @BeforeEach
    void setUp() {
        restaurant = Restaurant.builder()
                .name("Test Restaurant")
                .build();

        // 생성시 자동으로 restaurant의 리스트에 추가
        new RestaurantCharacteristic(restaurant, RestaurantAspect.TASTE, 4.0);
        new RestaurantCharacteristic(restaurant, RestaurantAspect.ATMOSPHERE, 3.5);

        userPreference = UserPreference.createForUser(User.builder().username("testUser").build());
        userPreference.addAspectPreference(RestaurantAspect.TASTE, 5.0);
        userPreference.addAspectPreference(RestaurantAspect.ATMOSPHERE, 3.0);

        User user1 = User.builder().username("user1").build();
        User user2 = User.builder().username("user2").build();

        Review review1 = Review.createReview(user1, restaurant, "Great taste");
        ReviewRating.createReviewRating(review1, RestaurantAspect.TASTE, 4.5);
        ReviewRating.createReviewRating(review1, RestaurantAspect.ATMOSPHERE, 4.0);

        Review review2 = Review.createReview(user2, restaurant, "Nice atmosphere.");
        ReviewRating.createReviewRating(review2, RestaurantAspect.TASTE, 3.5);
        ReviewRating.createReviewRating(review2, RestaurantAspect.ATMOSPHERE, 3.0);

        reviews = List.of(review1, review2);
    }

    @Nested
    @DisplayName("calculateRating 메서드는")
    class CalculateRatingTest {

        @Test
        @DisplayName("사용자 선호도가 캐시에 있을 때 정상적으로 평점을 계산한다.")
        void calculateRatingWhenUserPreferenceInCache() {
            String username = "testUser";
            personalizedRatingService.handleUserPreferenceUpdated(
                    new UserPreferenceUpdatedEvent(null, username, userPreference));
            when(reviewViewService.getReviewsForRestaurant(restaurant.getId())).thenReturn(reviews);

            double rating = personalizedRatingService.calculateRating(username, restaurant);

            assertTrue(rating > 0 && rating <= 5);
            verify(reviewViewService).getReviewsForRestaurant(restaurant.getId());
        }

        @Test
        @DisplayName("사용자 선호도가 캐시에 없을 때 이벤트를 발행하고 기본 평점을 반환한다.")
        void publishEventAndReturnDefaultRatingWhenUserPreferenceNotInCache() {
            String username = "newUser";
            double rating = personalizedRatingService.calculateRating(username, restaurant);

            assertEquals(0.0, rating);
            verify(eventPublisher).publish(argThat(event ->
                    event instanceof UserPreferenceRequestEvent &&
                            ((UserPreferenceRequestEvent) event).getUsername().equals(username)));
        }
    }

    @Nested
    @DisplayName("handleUserPreferenceUpdated 메서드는")
    class HandleUserPreferenceUpdatedTest {

        @Test
        @DisplayName("사용자 선호도 업데이트 이벤트를 처리하고 캐시를 갱신한다.")
        void updateUserPreferenceCache() {
            String username = "testUser";
            UserPreferenceUpdatedEvent event = new UserPreferenceUpdatedEvent(null, username, userPreference);

            personalizedRatingService.handleUserPreferenceUpdated(event);

            double rating = personalizedRatingService.calculateRating(username, restaurant);
            assertTrue(rating > 0 && rating <= 5);
        }
    }
}