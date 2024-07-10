package com.matzip.api.domain.restaurant.service;

import com.matzip.api.domain.external.dto.RestaurantSearchResponse;
import com.matzip.api.domain.restaurant.dto.RestaurantSearchCriteria;
import com.matzip.api.restaurant.dto.RestaurantSearchResult;

/**
 * 레스토랑 정보의 조회 및 검색을 처리하는 서비스 인터페이스입니다. 이 인터페이스는 CQRS 패턴의 Query 부분을 담당합니다.
 */
public interface RestaurantQueryService {

    /**
     * 주어진 검색 기준에 따라 레스토랑을 검색하고, 결과를 추천 순으로 정렬하여 반환합니다.
     *
     * @param criteria 검색 기준
     * @return 검색 및 추천 결과
     */
    RestaurantSearchResult searchAndRecommendRestaurants(RestaurantSearchCriteria criteria);

    /**
     * 특정 레스토랑의 상세 정보를 조회합니다.
     *
     * @param restaurantTitle 레스토랑 제목 (네이버 API에서는 고유 식별자로 title을 사용)
     * @param userId          조회하는 사용자 ID (개인화된 정보 제공을 위해)
     * @return 레스토랑 상세 정보
     */
    RestaurantSearchResult.RestaurantDto getRestaurantDetails(String restaurantTitle, String userId);

    /**
     * 사용자의 위치를 기반으로 주변의 인기 있는 레스토랑을 추천합니다.
     *
     * @param latitude  위도
     * @param longitude 경도
     * @param userId    사용자 ID
     * @return 추천된 레스토랑 목록
     */
    RestaurantSearchResult getPopularRestaurantsNearby(double latitude, double longitude, String userId);

    /**
     * RestaurantSearchResponse를 RestaurantSearchResult로 변환합니다.
     *
     * @param response RestaurantSearchResponse 객체
     * @return 변환된 RestaurantSearchResult 객체
     */
    RestaurantSearchResult convertToSearchResult(RestaurantSearchResponse response);

    /**
     * 특정 사용자에 대한 특정 레스토랑의 개인화된 평점을 계산합니다. 이 메서드는 사용자의 선호도와 레스토랑의 특성을 고려하여 개인화된 평점을 생성합니다.
     *
     * @param userId       사용자 ID
     * @param restaurantId 레스토랑 ID
     * @return 개인화된 평점 (0.0 ~ 5.0 사이의 값)
     * @throws IllegalArgumentException 레스토랑을 찾을 수 없는 경우
     */
    double getPersonalizedRating(String userId, Long restaurantId);
}