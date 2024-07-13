package com.matzip.api.domain.recommendation.repository;

import com.matzip.api.domain.recommendation.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
}
