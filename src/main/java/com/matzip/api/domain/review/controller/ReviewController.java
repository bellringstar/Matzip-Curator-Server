package com.matzip.api.domain.review.controller;

import com.matzip.api.common.api.Api;
import com.matzip.api.domain.review.dto.ReviewDto;
import com.matzip.api.domain.review.dto.ReviewFilterRequestDto;
import com.matzip.api.domain.review.dto.ReviewRequestDto;
import com.matzip.api.domain.review.entity.ReviewAuthorFactory;
import com.matzip.api.domain.review.entity.vo.ReviewAuthor;
import com.matzip.api.domain.review.service.ReviewQueryService;
import com.matzip.api.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/filter")
    public Page<ReviewDto> getReviews(
            @RequestBody ReviewFilterRequestDto filterRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdDate") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
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
