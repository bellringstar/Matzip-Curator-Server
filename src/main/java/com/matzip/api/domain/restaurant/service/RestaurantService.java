package com.matzip.api.domain.restaurant.service;

import com.matzip.api.domain.restaurant.dto.RestaurantCreateRequest;
import com.matzip.api.domain.restaurant.dto.RestaurantUpdateRequest;
import com.matzip.api.domain.restaurant.entity.Restaurant;
import java.util.List;

public interface RestaurantService {

    /**
     * 새로운 레스토랑을 생성합니다.
     *
     * @param request 레스토랑 생성 요청 정보
     * @return 생성된 레스토랑 정보
     */
    Restaurant createRestaurant(RestaurantCreateRequest request);

    /**
     * 기존 레스토랑 정보를 수정합니다.
     *
     * @param id      레스토랑 ID
     * @param request 레스토랑 수정 요청 정보
     * @return 수정된 레스토랑 정보
     */
    Restaurant updateRestaurant(Long id, RestaurantUpdateRequest request);

    /**
     * 레스토랑 정보를 삭제합니다.
     *
     * @param id 레스토랑 ID
     */
    void deleteRestaurant(Long id);

    /**
     * 외부 API와 동기화하여 레스토랑 정보를 갱신합니다.
     */
    void syncWithExternalApi();

    /**
     * 여러 개의 레스토랑을 배치로 생성합니다.
     *
     * @param requests 레스토랑 생성 요청 목록
     * @return 생성된 레스토랑 목록
     */
    List<Restaurant> createRestaurantsBatch(List<RestaurantCreateRequest> requests);

    /**
     * 여러 개의 레스토랑을 배치로 수정합니다.
     *
     * @param requests 레스토랑 수정 요청 목록
     * @return 수정된 레스토랑 목록
     */
    List<Restaurant> updateRestaurantsBatch(List<RestaurantUpdateRequest> requests);

}
