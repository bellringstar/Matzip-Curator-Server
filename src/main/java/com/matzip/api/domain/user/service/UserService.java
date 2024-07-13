package com.matzip.api.domain.user.service;

import com.matzip.api.common.event.DomainEventPublisher;
import com.matzip.api.domain.user.dto.UserCreateRequestDto;
import com.matzip.api.domain.user.entity.User;
import com.matzip.api.domain.user.event.UserCreatedEvent;
import com.matzip.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final DomainEventPublisher eventPublisher;

    public void createUser(UserCreateRequestDto requestDto) {

        User user = User.createUser(requestDto);
        user = userRepository.save(user);
        // 이벤트 발생
        eventPublisher.publish(new UserCreatedEvent(user));
    }
}
