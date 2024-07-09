package com.matzip.api.domain.external.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.matzip.api.common.error.ExternalApiErrorCode;
import com.matzip.api.common.exception.ApiException;
import com.matzip.api.domain.external.dto.NaverSearchResponse;
import com.matzip.api.domain.external.dto.RestaurantSearchResponse;
import com.matzip.api.domain.external.service.NaverSearchServiceImpl;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class NaverSearchServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NaverSearchServiceImpl naverSearchService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(naverSearchService, "clientId", "test-client-id");
        ReflectionTestUtils.setField(naverSearchService, "clientSecret", "test-client-secret");
    }

    @Test
    @DisplayName("성공적으로 레스토랑 검색 결과를 반환한다")
    void searchRestaurants_success() throws URISyntaxException {
        NaverSearchResponse mockResponse = mock(NaverSearchResponse.class);
        when(restTemplate.exchange(any(RequestEntity.class), eq(NaverSearchResponse.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        RestaurantSearchResponse response = naverSearchService.searchRestaurants(37.5665, 126.9780, "restaurant", 1000);

        assertThat(response).isNotNull();
        verify(restTemplate).exchange(any(RequestEntity.class), eq(NaverSearchResponse.class));
    }

    @Test
    @DisplayName("URI 구문 오류 발생 시 ApiException을 던진다")
    void searchRestaurants_uriSyntaxException() {
        assertThatThrownBy(() -> naverSearchService.searchRestaurants(37.5665, 126.9780, "invalid query", 1000))
                .isInstanceOf(ApiException.class)
                .extracting("errorCodeIfs")
                .extracting("errorCode")
                .isEqualTo(ExternalApiErrorCode.EXTERNAL_API_REQUEST_FAIL.getErrorCode());
    }

    @Test
    @DisplayName("API 요청 실패 시 ApiException을 던진다")
    void searchRestaurants_apiRequestFailure() throws URISyntaxException {
        when(restTemplate.exchange(any(RequestEntity.class), eq(NaverSearchResponse.class)))
                .thenThrow(new RuntimeException("API request failed"));

        assertThatThrownBy(() -> naverSearchService.searchRestaurants(37.5665, 126.9780, "restaurant", 1000))
                .isInstanceOf(ApiException.class)
                .extracting("errorCodeIfs")
                .extracting("errorCode")
                .isEqualTo(ExternalApiErrorCode.EXTERNAL_API_REQUEST_FAIL.getErrorCode());
    }
}
