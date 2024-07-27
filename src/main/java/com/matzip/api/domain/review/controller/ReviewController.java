package com.matzip.api.domain.review.controller;

import com.matzip.api.common.api.Api;
import com.matzip.api.domain.recommendation.enums.RestaurantAspect;
import com.matzip.api.domain.review.dto.ReviewDto;
import com.matzip.api.domain.review.dto.ReviewFilterCriteria;
import com.matzip.api.domain.review.dto.ReviewFilterRequestDto;
import com.matzip.api.domain.review.dto.ReviewRequestDto;
import com.matzip.api.domain.review.entity.ReviewAuthorFactory;
import com.matzip.api.domain.review.entity.vo.ReviewAuthor;
import com.matzip.api.domain.review.service.ReviewQueryService;
import com.matzip.api.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import java.util.EnumMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewQueryService reviewQueryService;
    private final ReviewAuthorFactory reviewAuthorFactory;

    @GetMapping
    public Page<ReviewDto> getReviews(
            @ModelAttribute ReviewFilterCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdDate") String sort
    ) {
        Map<RestaurantAspect, Double> aspectRatings = new EnumMap<>(RestaurantAspect.class);
        if (criteria.getPrice() != null) aspectRatings.put(RestaurantAspect.PRICE, criteria.getPrice());
        if (criteria.getTaste() != null) aspectRatings.put(RestaurantAspect.TASTE, criteria.getTaste());
        if (criteria.getAtmosphere() != null) aspectRatings.put(RestaurantAspect.ATMOSPHERE, criteria.getAtmosphere());
        if (criteria.getPortion() != null) aspectRatings.put(RestaurantAspect.PORTION, criteria.getPortion());
        if (criteria.getNoiseLevel() != null) aspectRatings.put(RestaurantAspect.NOISE_LEVEL, criteria.getNoiseLevel());
        if (criteria.getService() != null) aspectRatings.put(RestaurantAspect.SERVICE, criteria.getService());

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        ReviewFilterRequestDto filterRequest = new ReviewFilterRequestDto(criteria.getRestaurantId(), aspectRatings);
        Page<ReviewDto> filteredReviews = reviewQueryService.getFilteredReviews(filterRequest, pageable);
        return filteredReviews;
    }

    @PostMapping
    public Api<ReviewDto> createReview(@Valid @RequestBody ReviewRequestDto request, Authentication authentication) {
        ReviewAuthor reviewAuthor = reviewAuthorFactory.createFromSecurityContext(authentication);
        ReviewDto review = reviewService.createReview(request, reviewAuthor);
        return Api.OK(review);
    }
}
