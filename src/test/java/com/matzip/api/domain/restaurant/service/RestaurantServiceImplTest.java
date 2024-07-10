package com.matzip.api.domain.restaurant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.matzip.api.domain.external.dto.RestaurantSearchResponse;
import com.matzip.api.domain.external.service.RestaurantSearchService;
import com.matzip.api.domain.restaurant.dto.LocationSearchRequest;
import com.matzip.api.domain.restaurant.dto.RestaurantSyncResult;
import com.matzip.api.domain.restaurant.entity.Restaurant;
import com.matzip.api.domain.restaurant.repository.RestaurantRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("RestaurantService 구현 테스트")
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantSearchService restaurantSearchService;

    @Mock
    private PersonalizedRatingService personalizedRatingService;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("새로운 레스토랑 동기화 - 새 레스토랑이 추가되어야 함")
    void synchronizeExternalData_WhenNewRestaurants_ShouldAddThem() {
        // Given
        RestaurantSearchResponse.Restaurant newRestaurant = new RestaurantSearchResponse.Restaurant();
        newRestaurant.setTitle("New Restaurant");
        RestaurantSearchResponse response = new RestaurantSearchResponse();
        response.setRestaurants(List.of(newRestaurant));

        when(restaurantRepository.findByExternalId(anyString())).thenReturn(Optional.empty());
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        RestaurantSyncResult result = restaurantService.synchronizeExternalData(response);

        // Then
        assertEquals(1, result.getNewlyAdded());
        assertEquals(1, result.getTotalProcessed());
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("기존 레스토랑 동기화 - 변경 없는 경우 수정되지 않아야 함")
    void synchronizeExternalData_WhenExistingRestaurantsUnchanged_ShouldNotModifyThem() {
        // Given
        RestaurantSearchResponse.Restaurant existingRestaurant = new RestaurantSearchResponse.Restaurant();
        existingRestaurant.setTitle("Existing Restaurant");
        String externalId = existingRestaurant.generateId();
        RestaurantSearchResponse response = new RestaurantSearchResponse();
        response.setRestaurants(List.of(existingRestaurant));

        Restaurant dbRestaurant = new Restaurant(externalId, "Existing Restaurant");
        when(restaurantRepository.findByExternalId(anyString())).thenReturn(Optional.of(dbRestaurant));

        // When
        RestaurantSyncResult result = restaurantService.synchronizeExternalData(response);

        // Then
        assertEquals(1, result.getUnchanged());
        assertEquals(1, result.getTotalProcessed());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    @DisplayName("기존 레스토랑 동기화 - 변경된 경우 비활성화 후 새로 추가되어야 함")
    void synchronizeExternalData_WhenExistingRestaurantsChanged_ShouldDeactivateAndAddNew() {
        // Given
        RestaurantSearchResponse.Restaurant changedRestaurant = new RestaurantSearchResponse.Restaurant();
        changedRestaurant.setTitle("Changed Restaurant");
        RestaurantSearchResponse response = new RestaurantSearchResponse();
        response.setRestaurants(List.of(changedRestaurant));

        Restaurant dbRestaurant = new Restaurant("externalId", "Old Restaurant");
        when(restaurantRepository.findByExternalId(anyString())).thenReturn(Optional.of(dbRestaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        RestaurantSyncResult result = restaurantService.synchronizeExternalData(response);

        // Then
        assertEquals(1, result.getNewlyAdded());
        assertEquals(1, result.getDeactivated());
        assertEquals(1, result.getTotalProcessed());
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
        assertTrue(dbRestaurant.isDeactivated());
    }

    @Test
    @DisplayName("위치 기반 레스토랑 조회 및 동기화 - 외부 서비스 호출 및 동기화 수행")
    void fetchAndSynchronizeRestaurants_ShouldCallExternalServiceAndSynchronize() {
        // Given
        LocationSearchRequest request = new LocationSearchRequest(37.5665, 126.9780, "음식점", 1000);
        RestaurantSearchResponse mockResponse = new RestaurantSearchResponse();
        mockResponse.setRestaurants(List.of(new RestaurantSearchResponse.Restaurant()));

        when(restaurantSearchService.searchRestaurants(anyDouble(), anyDouble(), anyString(), anyInt()))
                .thenReturn(mockResponse);

        // When
        RestaurantSyncResult result = restaurantService.fetchAndSynchronizeRestaurants(request);

        // Then
        verify(restaurantSearchService).searchRestaurants(37.5665, 126.9780, "음식점", 1000);
        assertNotNull(result);
    }

}