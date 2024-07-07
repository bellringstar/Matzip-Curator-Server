package com.matzip.api.domain.cache.service;

import com.matzip.api.domain.restaurant.entity.Restaurant;
import java.util.List;

public interface CacheService {
    void cacheSearchResults(String cacheKey, List<Restaurant> restaurants);

    List<Restaurant> getCachedSearchResults(String cacheKey);

    void cacheRestaurantDetails(String externalId, Restaurant restaurant);

    Restaurant getCachedRestaurantDetails(String externalId);

    void evictCache(String cacheKey);
}
