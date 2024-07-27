package com.matzip.api.domain.restaurant.entity;

import com.matzip.api.common.entity.BaseTimeEntity;
import com.matzip.api.domain.review.entity.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "restaurants")
public class Restaurant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(name = "external_id", unique = true)
    private String externalId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestaurantCharacteristic> characteristics = new ArrayList<>();

//    @OneToMany(mappedBy = "restaurant_id")
//    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Restaurant(String externalId, String name) {
        this.externalId = externalId;
        this.name = name;
    }

    public void addCharacteristic(RestaurantCharacteristic characteristic) {
        characteristics.add(characteristic);
    }

    public void updateExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean isDeactivated() {
        return this.active == false;
    }
}
