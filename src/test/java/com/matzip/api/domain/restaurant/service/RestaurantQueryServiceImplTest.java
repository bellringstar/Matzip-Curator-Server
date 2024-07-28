package com.matzip.api.domain.restaurant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.matzip.api.common.event.DomainEventPublisher;
import com.matzip.api.domain.external.dto.RestaurantSearchResponse;
import com.matzip.api.domain.external.service.RestaurantSearchService;
import com.matzip.api.domain.restaurant.dto.RestaurantSearchCriteria;
import com.matzip.api.domain.restaurant.dto.RestaurantSearchResult;
import com.matzip.api.domain.restaurant.entity.Restaurant;
import com.matzip.api.domain.restaurant.repository.RestaurantRepository;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantQueryServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantSearchService restaurantSearchService;

    @Mock
    private PersonalizedRatingService personalizedRatingService;

    @Mock
    private DomainEventPublisher eventPublisher;

    private RestaurantQueryServiceImpl restaurantQueryService;

    private RestaurantSearchCriteria criteria;

    @BeforeEach
    void setUp() {
        restaurantQueryService = new RestaurantQueryServiceImpl(
                restaurantRepository,
                restaurantSearchService,
                personalizedRatingService,
                eventPublisher
        );
        criteria = RestaurantSearchCriteria.of(37.5665, 126.9780, "맛집", 1000, "user1", "relevance");
    }

    @Test
    @DisplayName("추천 레스토랑을 성공적으로 반환한다.")
    void searchAndRecommendRestaurants_Success() {
        // Given
        RestaurantSearchResponse.Restaurant externalRestaurant1 = new RestaurantSearchResponse.Restaurant(
                "Restaurant 1", "Category", "Address", "RoadAddress", "010-1234-5678", "1", "1");
        RestaurantSearchResponse.Restaurant externalRestaurant2 = new RestaurantSearchResponse.Restaurant(
                "Restaurant 2", "Category", "Address", "RoadAddress", "010-2345-6789", "2", "2");
        RestaurantSearchResponse externalResponse = new RestaurantSearchResponse();
        externalResponse.setRestaurants(Arrays.asList(externalRestaurant1, externalRestaurant2));

        Restaurant internalRestaurant = new Restaurant("ext1", "Restaurant 1");

        when(restaurantSearchService.searchRestaurants(anyDouble(), anyDouble(), anyString(), anyInt())).thenReturn(
                externalResponse);
        when(restaurantRepository.findByExternalId("Restaurant 1:1:1")).thenReturn(Optional.of(internalRestaurant));
        when(restaurantRepository.findByExternalId("Restaurant 2:2:2")).thenReturn(Optional.empty());
        when(personalizedRatingService.calculateRating(eq("user1"), any(Restaurant.class))).thenReturn(4.5);

        // When
        RestaurantSearchResult result = restaurantQueryService.searchAndRecommendRestaurants(criteria);
        System.out.println(result);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getRestaurants().size());
        assertEquals("Restaurant 1", result.getRestaurants().get(0).getTitle());
        assertEquals(4.5, result.getRestaurants().get(0).getRelevanceScore(), 0.01);

        verify(eventPublisher, times(1)).publish(any());
    }

    @Test
    @DisplayName("레스토랑이 비어있다면 비어있는 결과를 반환한다.")
    void searchAndRecommendRestaurants_NoResults() {
        // Given
        RestaurantSearchResponse externalResponse = new RestaurantSearchResponse();

        when(restaurantSearchService.searchRestaurants(anyDouble(), anyDouble(), anyString(), anyInt())).thenReturn(
                externalResponse);

        // When
        RestaurantSearchResult result = restaurantQueryService.searchAndRecommendRestaurants(criteria);

        // Then
        assertNotNull(result);
        assertTrue(result.getRestaurants().isEmpty());
    }

    @Test
    @DisplayName("외부 요청이 실패하면 예외를 발생시킨다.")
    void searchAndRecommendRestaurants_ExternalServiceFailure() {
        // Given

        when(restaurantSearchService.searchRestaurants(anyDouble(), anyDouble(), anyString(), anyInt())).thenThrow(
                new RuntimeException("External API failure"));

        // When & Then
        assertThrows(RuntimeException.class, () -> restaurantQueryService.searchAndRecommendRestaurants(criteria));
    }
}