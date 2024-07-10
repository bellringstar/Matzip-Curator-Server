package com.matzip.api.domain.restaurant.service;

import com.matzip.api.domain.external.dto.RestaurantSearchResponse;
import com.matzip.api.domain.restaurant.dto.LocationSearchRequest;
import com.matzip.api.domain.restaurant.dto.RestaurantSyncResult;
import com.matzip.api.domain.restaurant.entity.Restaurant;
import java.util.Map;

public interface RestaurantService {

    /**
     * 외부 API에서 가져온 레스토랑 데이터를 동기화합니다. 이 메서드는 외부 API에서 받아온 데이터를 내부 데이터베이스에 저장하거나 업데이트합니다.
     *
     * @param restaurantSearchResponse 외부 API에서 가져온 레스토랑 데이터
     * @return 동기화 결과를 포함한 RestaurantSyncResult 객체
     */
    RestaurantSyncResult synchronizeExternalData(RestaurantSearchResponse restaurantSearchResponse);

    /**
     * 특정 위치 기반으로 레스토랑 데이터를 외부 API에서 가져와 동기화합니다. 이 메서드는 외부 API를 호출하고, 받아온 데이터를 synchronizeExternalData 메서드를 통해 동기화합니다.
     *
     * @param request 위치 검색 요청 정보를 담고 있는 객체
     * @return 동기화 결과를 포함한 RestaurantSyncResult 객체
     */
    RestaurantSyncResult fetchAndSynchronizeRestaurants(LocationSearchRequest request);

    /**
     * 레스토랑의 특성 정보를 업데이트합니다. 이 메서드는 레스토랑의 특성(예: 분위기, 가격대 등)이 변경될 때 호출됩니다.
     *
     * @param restaurantId    업데이트할 레스토랑의 ID
     * @param characteristics 업데이트할 특성 정보
     */
    void updateRestaurantCharacteristics(Long restaurantId, Map<String, Double> characteristics);
}


