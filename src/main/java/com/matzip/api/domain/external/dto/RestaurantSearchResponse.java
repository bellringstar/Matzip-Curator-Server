package com.matzip.api.domain.external.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class RestaurantSearchResponse {
    private List<Restaurant> restaurants;

    public RestaurantSearchResponse(NaverSearchResponse naverSearchResponse) {
        this.restaurants = naverSearchResponse.getItems().stream()
                .map(item -> new Restaurant(
                        item.getTitle(),
                        item.getCategory(),
                        item.getAddress(),
                        item.getRoadAddress(),
                        item.getTelephone(),
                        item.getMapx(),
                        item.getMapy()
                ))
                .collect(Collectors.toList());
    }

    @Data
    public static class Restaurant {
        private String title;
        private String category;
        private String address;
        private String roadAddress;
        private String telephone;
        private String mapx;
        private String mapy;

        public Restaurant(String title, String category, String address, String roadAddress, String telephone,
                          String mapx, String mapy) {
            this.title = title;
            this.category = category;
            this.address = address;
            this.roadAddress = roadAddress;
            this.telephone = telephone;
            this.mapx = mapx;
            this.mapy = mapy;
        }
    }
}
