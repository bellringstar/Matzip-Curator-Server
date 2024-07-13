package com.matzip.api.domain.user.repository;

import com.matzip.api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
