package com.matzip.api.domain.restaurant.service;

import com.matzip.api.domain.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantViewService {

    /**
     * 검색어(query), 현재 위치(latitude, longitude), 반경(radius)를 기준으로 레스토랑을 검색합니다.
     *
     * @param latitude  현재 위치의 위도
     * @param longitude 현재 위치의 경도
     * @param query     검색어
     * @param radius    검색 반경(km)
     * @param pageable  페이징 정보
     * @return 검색된 레스토랑 목록
     */
    Page<Restaurant> searchRestaurants(double latitude, double longitude, String query, int radius, Pageable pageable);

    /**
     * ID를 기준으로 레스토랑 정보를 조회합니다.
     *
     * @param id 레스토랑 ID
     * @return 레스토랑 정보
     */
    Restaurant getRestaurantById(Long id);

    /**
     * 외부 시스템의 ID를 기준으로 레스토랑 정보를 조회합니다.
     *
     * @param externalId 외부 시스템의 레스토랑 ID
     * @return 레스토랑 정보
     */
    Restaurant getRestaurantByExternalId(String externalId);

    /**
     * 특정 조건에 따라 추천 레스토랑을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 추천 레스토랑 목록
     */
    Page<Restaurant> getRecommendedRestaurants(Long userId, Pageable pageable);

    /**
     * 특정 카테고리의 레스토랑 목록을 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @param pageable 페이징 정보
     * @return 카테고리별 레스토랑 목록
     */
    Page<Restaurant> getRestaurantsByCategory(Long categoryId, Pageable pageable);
}

