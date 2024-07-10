package com.matzip.api.domain.restaurant.entity;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "restaurant_characteristics")
public class RestaurantCharacteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RestaurantAspect aspect;

    @Column(nullable = false)
    private Double score;

    @Builder
    public RestaurantCharacteristic(Restaurant restaurant, RestaurantAspect aspect, Double score) {
        this.restaurant = restaurant;
        this.aspect = aspect;
        this.score = score;
    }
}
