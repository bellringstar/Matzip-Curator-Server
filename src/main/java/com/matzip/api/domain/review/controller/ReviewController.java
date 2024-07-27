package com.matzip.api.domain.review.controller;

import com.matzip.api.common.api.Api;
import com.matzip.api.domain.review.dto.ReviewDto;
import com.matzip.api.domain.review.dto.ReviewRequestDto;
import com.matzip.api.domain.review.entity.ReviewAuthorFactory;
import com.matzip.api.domain.review.entity.vo.ReviewAuthor;
import com.matzip.api.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewAuthorFactory reviewAuthorFactory;

    @PostMapping
    public Api<ReviewDto> createReview(@Valid @RequestBody ReviewRequestDto request, Authentication authentication) {
        ReviewAuthor reviewAuthor = reviewAuthorFactory.createFromSecurityContext(authentication);
        ReviewDto review = reviewService.createReview(request, reviewAuthor);
        return Api.OK(review);
    }
}
