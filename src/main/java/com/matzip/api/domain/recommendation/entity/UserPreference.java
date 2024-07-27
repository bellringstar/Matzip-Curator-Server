package com.matzip.api.domain.recommendation.entity;

import com.matzip.api.common.entity.BaseTimeEntity;
import com.matzip.api.domain.recommendation.enums.RestaurantAspect;
import com.matzip.api.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_preferences")
public class UserPreference extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_preference_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "userPreference", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AspectPreference> aspectPreferences = new ArrayList<>();

    public static UserPreference createForUser(User user) {
        UserPreference userPreference = new UserPreference();
        userPreference.user = user;
        return userPreference;
    }

    public void addAspectPreference(RestaurantAspect aspect, Double score) {
        AspectPreference preference = new AspectPreference(this, aspect, score);
        aspectPreferences.add(preference);
    }

    public void updateAspectPreference(RestaurantAspect aspect, Double score) {
        aspectPreferences.stream()
                .filter(pref -> pref.getAspect() == aspect)
                .findFirst()
                .ifPresentOrElse(
                        pref -> pref.updateScore(score),
                        () -> addAspectPreference(aspect, score)
                );
    }

    public void adjustAspectWeight(RestaurantAspect aspect, Double adjustment) {
        aspectPreferences.stream()
                .filter(pref -> pref.getAspect() == aspect)
                .findFirst()
                .ifPresent(pref -> pref.adjustWeight(adjustment));
    }

    public Double getAspectScore(RestaurantAspect aspect) {
        return aspectPreferences.stream()
                .filter(pref -> pref.getAspect() == aspect)
                .findFirst()
                .map(AspectPreference::getScore)
                .orElse(0.0);
    }

    public Double getWeightedAspectScore(RestaurantAspect aspect) {
        return aspectPreferences.stream()
                .filter(pref -> pref.getAspect() == aspect)
                .findFirst()
                .map(AspectPreference::getWeightedScore)
                .orElse(0.0);
    }
}
