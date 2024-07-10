package com.matzip.api.domain.restaurant.service;

import com.matzip.api.common.error.RestaurantErrorCode;
import com.matzip.api.common.exception.ApiException;
import com.matzip.api.domain.external.dto.RestaurantSearchResponse;
import com.matzip.api.domain.external.service.RestaurantSearchService;
import com.matzip.api.domain.restaurant.dto.LocationSearchRequest;
import com.matzip.api.domain.restaurant.dto.RestaurantSyncResult;
import com.matzip.api.domain.restaurant.entity.Restaurant;
import com.matzip.api.domain.restaurant.repository.RestaurantRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantSearchService restaurantSearchService;
    private final PersonalizedRatingService personalizedRatingService;

    @Override
    public RestaurantSyncResult synchronizeExternalData(RestaurantSearchResponse restaurantSearchResponse) {
        int totalProcessed = 0;
        int newlyAdded = 0;
        int unchanged = 0;
        int deactivated = 0;

        Set<String> processedExternalIds = restaurantSearchResponse.getRestaurants().stream()
                .map(RestaurantSearchResponse.Restaurant::generateId)
                .collect(Collectors.toSet());

        for (RestaurantSearchResponse.Restaurant externalRestaurant : restaurantSearchResponse.getRestaurants()) {
            String externalId = externalRestaurant.generateId();
            Optional<Restaurant> existingRestaurant = restaurantRepository.findByExternalId(externalId);

            if (existingRestaurant.isPresent()) {
                String currentName = existingRestaurant.get().getName();
                String currentExternalId = existingRestaurant.get().getExternalId();

                if (!currentName.equals(externalRestaurant.getTitle()) || !currentExternalId.equals(externalId)) {
                    createNewRestaurant(externalRestaurant);
                    existingRestaurant.get().deactivate();
                    newlyAdded++;
                    deactivated++;
                } else {
                    unchanged++;
                }
            } else {
                createNewRestaurant(externalRestaurant);
                newlyAdded++;
            }
            totalProcessed++;
        }

        deactivated += deactivateNotFoundRestaurants(processedExternalIds);

        return RestaurantSyncResult.of(totalProcessed, newlyAdded, unchanged, deactivated);
    }

    @Override
    public RestaurantSyncResult fetchAndSynchronizeRestaurants(LocationSearchRequest request) {
        RestaurantSearchResponse response = restaurantSearchService.searchRestaurants(
                request.getLatitude(), request.getLongitude(), request.getQuery(), request.getRadius());
        return synchronizeExternalData(response);
    }

    @Override
    public void updateRestaurantCharacteristics(Long restaurantId, Map<String, Double> characteristics) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ApiException(RestaurantErrorCode.RESTAURANT_NOT_FOUND));

        //TODO:  특성 업데이트 로직 추가
        /*
        * 무엇을 업데이트 해야하는가..
        * 특성에 있는 해당 aspect의 score를 업데이트해야한다. 해당 정보가 map에 들어있을것이다.
        * */
    }

    private Restaurant createNewRestaurant(RestaurantSearchResponse.Restaurant externalRestaurant) {
        Restaurant newRestaurant = Restaurant.builder()
                .externalId(externalRestaurant.generateId())
                .name(externalRestaurant.getTitle())
                .build();

        return restaurantRepository.save(newRestaurant);
    }

    private int deactivateNotFoundRestaurants(Set<String> processedExternalIds) {
        List<Restaurant> activeRestaurants = restaurantRepository.findByActiveTrue();
        int deactivated = 0;
        for (Restaurant restaurant : activeRestaurants) {
            if (!processedExternalIds.contains(restaurant.getExternalId())) {
                restaurant.deactivate();
                deactivated++;
            }
        }
        return deactivated;
    }
}
