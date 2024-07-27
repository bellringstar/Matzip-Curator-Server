package com.matzip.api.domain.review.entity.vo;

import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class ReviewAuthor {

    private final Long id;

    private final String displayName;

    // JPA 스펙을 위한 기본 생성자
    protected ReviewAuthor() {
        this.id = null;
        this.displayName = null;
    }

    public ReviewAuthor(Long id, String displayName) {
        this.id = Objects.requireNonNull(id, "Author id must not be null");
        this.displayName = Objects.requireNonNull(displayName, "Author loginId must not be null");
    }
}
