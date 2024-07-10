package com.matzip.api.domain.restaurant.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestaurantSyncResult {
    private final int totalProcessed;
    private final int newlyAdded;
    private final int unchanged;
    private final int deactivated;

    public static RestaurantSyncResult of(int totalProcessed, int newlyAdded, int unchanged, int deactivated) {
        return RestaurantSyncResult.builder()
                .totalProcessed(totalProcessed)
                .newlyAdded(newlyAdded)
                .deactivated(deactivated)
                .unchanged(unchanged)
                .build();
    }

    @Override
    public String toString() {
        return String.format("동기화 결과: 총 처리 %d, 신규 추가 %d, 변화없음 %d, 비활성화 %d",
                totalProcessed, newlyAdded, unchanged, deactivated);
    }
}
