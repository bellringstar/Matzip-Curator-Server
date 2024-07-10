package com.matzip.api.domain.restaurant.service;

import com.matzip.api.common.error.RestaurantErrorCode;
import com.matzip.api.common.exception.ApiException;
import com.matzip.api.domain.external.dto.RestaurantSearchResponse;
import com.matzip.api.domain.recommendation.service.UserPreferenceService;
import com.matzip.api.domain.restaurant.dto.RestaurantSearchCriteria;
import com.matzip.api.domain.restaurant.entity.Restaurant;
import com.matzip.api.domain.restaurant.repository.RestaurantRepository;
import com.matzip.api.domain.review.service.ReviewService;
import com.matzip.api.restaurant.dto.RestaurantSearchResult;
import com.matzip.api.restaurant.dto.RestaurantSearchResult.RestaurantDto;
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
    private final UserPreferenceService userPreferenceService;
    private final ReviewService reviewService;
    private final PersonalizedRatingService personalizedRatingService;

    @Override
    public RestaurantSearchResult searchAndRecommendRestaurants(RestaurantSearchCriteria criteria) {
        return null;
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
}
