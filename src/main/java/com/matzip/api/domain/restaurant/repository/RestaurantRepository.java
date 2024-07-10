package com.matzip.api.domain.restaurant.repository;

import com.matzip.api.domain.restaurant.entity.Restaurant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByExternalId(String externalId);

    List<Restaurant> findByActiveTrue();
}
