package com.matzip.api.domain.restaurant.service;

import com.matzip.api.domain.recommendation.entity.AspectPreference;
import com.matzip.api.domain.recommendation.entity.UserPreference;
import com.matzip.api.domain.recommendation.service.UserPreferenceService;
import com.matzip.api.domain.restaurant.entity.Restaurant;
import com.matzip.api.domain.restaurant.entity.RestaurantCharacteristic;
import com.matzip.api.domain.review.entity.Review;
import com.matzip.api.domain.review.service.ReviewViewService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonalizedRatingService implements RatingService {

    private final UserPreferenceService userPreferenceService;
    private final ReviewViewService reviewViewService;

    @Override
    public double calculateRating(String userId, Restaurant restaurant) {
        UserPreference userPreference = userPreferenceService.getUserPreference(userId);
        List<Review> reviews = reviewViewService.getReviewsForRestaurant(restaurant.getId());

        Map<String, Double> characteristicScores = restaurant.getCharacteristics().stream()
                .collect(Collectors.toMap(
                        characteristic -> characteristic.getAspect().name(),
                        RestaurantCharacteristic::getScore
                ));

        double personalizedScore = 0.0;
        double totalWeight = 0.0;

        for (AspectPreference aspectPreference : userPreference.getAspectPreferences()) {
            String aspect = aspectPreference.getAspect().name();
            double preferenceWeight = aspectPreference.getWeight();

            if (characteristicScores.containsKey(aspect)) {
                double characteristicScore = characteristicScores.get(aspect);
                personalizedScore += characteristicScore * preferenceWeight;
                totalWeight += preferenceWeight;
            }
        }

        if (totalWeight > 0) {
            personalizedScore /= totalWeight;
        }

        double collaborativeScore = calculateCollaborativeScore(userId, reviews);

        return (personalizedScore + collaborativeScore) / 2.0; //TODO: 가중치 고려 계산 변경
    }

    private double calculateCollaborativeScore(String userId, List<Review> reviews) {
        // TODO: 협업 필터링 로직 필요: 유사한 사용자들의 평점 편균 계산
        return 0;
    }
}
