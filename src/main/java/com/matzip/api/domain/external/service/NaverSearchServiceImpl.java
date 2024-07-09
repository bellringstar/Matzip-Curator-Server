package com.matzip.api.domain.external.service;

import com.matzip.api.common.error.ExternalApiErrorCode;
import com.matzip.api.common.exception.ApiException;
import com.matzip.api.domain.external.dto.NaverSearchResponse;
import com.matzip.api.domain.external.dto.RestaurantSearchResponse;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverSearchServiceImpl implements RestaurantSearchService {

    private final String clientId;
    private final String clientSecret;
    private final RestTemplate restTemplate;

    private static final String API_URL = "https://openapi.naver.com/v1/search/local.json";

    public NaverSearchServiceImpl(RestTemplate restTemplate, @Value("${naver.client.secret}") String clientSecret,
                                  @Value("${naver.client.id}") String clientId) {
        this.restTemplate = restTemplate;
        this.clientSecret = clientSecret;
        this.clientId = clientId;
    }

    @Override
    public RestaurantSearchResponse searchRestaurants(double latitude, double longitude, String query, int radius) {
        try {
            URI uri = new URI(
                    API_URL + "?query=" + query + "&display=10&start=1&sort=random&latitude=" + latitude + "&longitude="
                            + longitude + "&radius=" + radius);

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);

            RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, uri);
            ResponseEntity<NaverSearchResponse> responseEntity = restTemplate.exchange(requestEntity,
                    NaverSearchResponse.class);

            return new RestaurantSearchResponse(responseEntity.getBody());
        } catch (Exception e) {
            throw new ApiException(ExternalApiErrorCode.EXTERNAL_API_REQUEST_FAIL, e);
        }
    }
}
