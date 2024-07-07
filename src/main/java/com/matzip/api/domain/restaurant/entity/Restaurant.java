package com.matzip.api.domain.restaurant.entity;

import com.matzip.api.common.entity.BaseTimeEntity;
import com.matzip.api.domain.review.entity.Review;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Location location;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Double averageRating;

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviews = new ArrayList<>();

    @Column(nullable = false)
    private String externalId;

    public static Restaurant createRestaurant(String name, Location location, String category, Double averageRating,
                                              String externalId) {
        Restaurant restaurant = new Restaurant();
        restaurant.name = name;
        restaurant.location = location;
        restaurant.category = category;
        restaurant.averageRating = 0.0;
        restaurant.externalId = externalId;

        return restaurant;
    }

    public void updateAverageRating() {
        this.averageRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public void addReview(Review review) {
        this.reviews.add(review);
        updateAverageRating();
    }

    public void removeReview(Review review) {
        this.reviews.remove(review);
        updateAverageRating();
    }
}
