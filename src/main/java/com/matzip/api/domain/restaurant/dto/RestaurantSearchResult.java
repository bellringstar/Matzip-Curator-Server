package com.matzip.api.domain.restaurant.dto;

import com.matzip.api.domain.external.dto.RestaurantSearchResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class RestaurantSearchResult {
    private final List<RestaurantDto> restaurants;

    @Getter
    @Builder
    @ToString
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
                    .relevanceScore(restaurant.getRelevanceScore())
                    .build();
        }
    }

    public static RestaurantSearchResult from(List<RestaurantDto> restaurantDtos) {
        return RestaurantSearchResult.builder()
                .restaurants(restaurantDtos)
                .build();
    }
}