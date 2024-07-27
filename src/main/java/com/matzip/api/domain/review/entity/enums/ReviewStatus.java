package com.matzip.api.domain.review.entity.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReviewStatus {
    ACTIVE("정상"),
    DELETED("삭제"),
    UNDER_REVIEW("검토중"),
    HIDDEN("숨김");

    private final String description;
}