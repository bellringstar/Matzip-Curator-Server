package com.matzip.api.domain.restaurant.service;

import com.matzip.api.common.error.RestaurantErrorCode;
import com.matzip.api.common.event.DomainEventPublisher;
import com.matzip.api.common.exception.ApiException;
import com.matzip.api.domain.external.dto.RestaurantSearchResponse;
import com.matzip.api.domain.external.service.RestaurantSearchService;
import com.matzip.api.domain.restaurant.dto.RestaurantSearchCriteria;
import com.matzip.api.domain.restaurant.dto.RestaurantSearchResult;
import com.matzip.api.domain.restaurant.dto.RestaurantSearchResult.RestaurantDto;
import com.matzip.api.domain.restaurant.entity.Restaurant;
import com.matzip.api.domain.restaurant.event.RestaurantCreateRequestEvent;
import com.matzip.api.domain.restaurant.repository.RestaurantRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RestaurantQueryServiceImpl implements
        RestaurantQueryService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantSearchService restaurantSearchService;
    private final PersonalizedRatingService personalizedRatingService;
    private final DomainEventPublisher eventPublisher;

    @Override
    public RestaurantSearchResult searchAndRecommendRestaurants(RestaurantSearchCriteria criteria) {
        RestaurantSearchResponse externalResponse = getExternalRestaurant(criteria);
        List<RestaurantSearchResult.RestaurantDto> restaurantDtos = new ArrayList<>();
        for (RestaurantSearchResponse.Restaurant externalRestaurant : externalResponse.getRestaurants()) {
            String externalId = externalRestaurant.generateId();
            Optional<Restaurant> byExternalId = restaurantRepository.findByExternalId(externalId);
            if (byExternalId.isEmpty()) {
                eventPublisher.publish(new RestaurantCreateRequestEvent(externalRestaurant));
                continue;
            }
            Restaurant internalRestaurant = byExternalId.get();

            double personalizedScore = personalizedRatingService.calculateRating(criteria.getUserId(),
                    internalRestaurant);
            externalRestaurant.setRelevanceScore(personalizedScore);
            RestaurantSearchResult.RestaurantDto dto = RestaurantSearchResult.RestaurantDto.from(externalRestaurant);
            restaurantDtos.add(dto);
        }
        restaurantDtos.sort(
                Comparator.comparingDouble(RestaurantSearchResult.RestaurantDto::getRelevanceScore).reversed());

        return RestaurantSearchResult.from(restaurantDtos);
    }

    @Override
    public RestaurantDto getRestaurantDetails(String restaurantTitle, String userId) {
        return null;
    }

    @Override
    public RestaurantSearchResult getPopularRestaurantsNearby(double latitude, double longitude, String userId) {
        return null;
    }

    @Override
    public RestaurantSearchResult convertToSearchResult(RestaurantSearchResponse response) {
        return null;
    }

    @Override
    public double getPersonalizedRating(String userId, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ApiException(RestaurantErrorCode.RESTAURANT_NOT_FOUND));

        return personalizedRatingService.calculateRating(userId, restaurant);
    }

    private RestaurantSearchResponse getExternalRestaurant(RestaurantSearchCriteria criteria) {
        return restaurantSearchService.searchRestaurants(
                criteria.getLatitude(),
                criteria.getLongitude(),
                criteria.getQuery(),
                criteria.getRadius());
    }
}
