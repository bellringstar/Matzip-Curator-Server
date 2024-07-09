package com.matzip.api.domain.external;

import com.matzip.api.domain.external.dto.RestaurantSearchResponse;
import com.matzip.api.domain.external.service.RestaurantSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestaurantSearchController {

    private RestaurantSearchService restaurantSearchService;

    @GetMapping("/search")
    public RestaurantSearchResponse search(@RequestParam double latitude, @RequestParam double longitude,
                                           @RequestParam String query, @RequestParam int radius) {
        return restaurantSearchService.searchRestaurants(latitude, longitude, query, radius);

    }
}
