package com.matzip.api.domain.recommendation.entity;

import com.matzip.api.domain.recommendation.enums.RestaurantAspect;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "aspect_preferences")
public class AspectPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_preference_id")
    private UserPreference userPreference;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RestaurantAspect aspect;

    @Column(nullable = false)
    private Double score;

    @Column(nullable = false)
    private Double weight;

    public static final Double DEFAULT_WEIGHT = 1.0;

    public AspectPreference(UserPreference userPreference, RestaurantAspect aspect, Double score) {
        this.userPreference = userPreference;
        this.aspect = aspect;
        this.score = score;
        this.weight = DEFAULT_WEIGHT;
    }

    public void updateScore(Double score) {
        this.score = score;
    }

    public void adjustWeight(Double adjustment) {
        // 사용자의 특정 행동(예를 들어 특정 부분에 자주 높은 점수를 준다) 이벤트 발생시 실행
        this.weight += adjustment;
        if (this.weight < 0) this.weight = 0.0;
        if (this.weight > 2) this.weight = 2.0;
    }

    public Double getWeightedScore() {
        return score * weight;
    }
}
