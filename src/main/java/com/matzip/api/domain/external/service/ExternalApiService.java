package com.matzip.api.domain.external.service;

import com.matzip.api.domain.external.dto.ExternalRestaurantResponse;
import java.util.List;

// TODO : 임시로 설정.. 외부 API 문서 보고 수정 예정
public interface ExternalApiService {
    List<ExternalRestaurantResponse> searchRestaurants(String query, double latitude, double longitude);
    ExternalRestaurantResponse getRestaurantDetails(String externalId);
}
