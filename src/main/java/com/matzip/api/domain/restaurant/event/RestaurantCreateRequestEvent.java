package com.matzip.api.domain.restaurant.event;

import com.matzip.api.common.event.DomainEvent;
import com.matzip.api.domain.external.dto.RestaurantSearchResponse;
import lombok.Value;

@Value
public class RestaurantCreateRequestEvent implements DomainEvent {

    RestaurantSearchResponse.Restaurant externalRestaurant;

    @Override
    public String getEventType() {
        return "RESTAURANT_CREATE_REQUEST";
    }
}
