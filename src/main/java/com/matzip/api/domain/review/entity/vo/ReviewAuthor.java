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
    private final String loginId;
    private final String name;

    // JPA 스펙을 위한 기본 생성자
    protected ReviewAuthor() {
        this.id = null;
        this.loginId = null;
        this.name = null;
    }

    public ReviewAuthor(Long id, String loginId, String name) {
        this.id = Objects.requireNonNull(id, "Author id must not be null");
        this.loginId = Objects.requireNonNull(loginId, "Author loginId must not be null");
        this.name = Objects.requireNonNull(name, "Author name must not be null");
    }
}
