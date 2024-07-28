package com.matzip.api.domain.external.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantSearchResponse {
    private List<Restaurant> restaurants = new ArrayList<>();


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
    @NoArgsConstructor
    public static class Restaurant {
        private String title;
        private String category;
        private String address;
        private String roadAddress;
        private String telephone;
        private String mapx;
        private String mapy;
        private Double relevanceScore = 0.0;

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

        public String generateId() {
            // TODO: 가게를 유일하게 식별할 수 있는 정보는 무엇인가...
            return title + ":" + mapx + ":" + mapy;
        }
    }
}
