package com.matzip.api.domain.user.service;

import com.matzip.api.common.event.DomainEventPublisher;
import com.matzip.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final DomainEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
}
