package com.matzip.api.domain.restaurant.dto;

import com.matzip.api.domain.external.dto.RestaurantSearchResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestaurantSearchResult {
    private final List<RestaurantDto> restaurants;

    @Getter
    @Builder
    public static class RestaurantDto {
        private final String title;
        private final String category;
        private final String address;
        private final String roadAddress;
        private final String telephone;
        private final String mapx;
        private final String mapy;
        private final double distance;
        private final double relevanceScore;

        public static RestaurantDto from(RestaurantSearchResponse.Restaurant restaurant) {
            return RestaurantDto.builder()
                    .title(restaurant.getTitle())
                    .category(restaurant.getCategory())
                    .address(restaurant.getAddress())
                    .roadAddress(restaurant.getRoadAddress())
                    .telephone(restaurant.getTelephone())
                    .mapx(restaurant.getMapx())
                    .mapy(restaurant.getMapy())
                    .build();
        }
    }

    public static RestaurantSearchResult from(RestaurantSearchResponse response) {
        List<RestaurantDto> restaurantDtos = response.getRestaurants().stream()
                .map(RestaurantDto::from)
                .collect(Collectors.toList());

        return RestaurantSearchResult.builder()
                .restaurants(restaurantDtos)
                .build();
    }
}