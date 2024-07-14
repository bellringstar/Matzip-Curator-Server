package com.matzip.api.security.service;

import com.matzip.api.common.error.JwtTokenErrorCode;
import com.matzip.api.common.error.UserErrorCode;
import com.matzip.api.common.event.DomainEventPublisher;
import com.matzip.api.common.exception.ApiException;
import com.matzip.api.common.util.ValidationUtils;
import com.matzip.api.domain.user.dto.UserResponseDto;
import com.matzip.api.domain.user.entity.User;
import com.matzip.api.domain.user.entity.UserPrincipal;
import com.matzip.api.domain.user.repository.UserRepository;
import com.matzip.api.security.jwt.TokenPair;
import com.matzip.api.security.dto.UserCreateRequestDto;
import com.matzip.api.security.event.UserCreatedEvent;
import com.matzip.api.security.jwt.JwtTokenProvider;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    public static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ValidationUtils validationUtils;
    private final DomainEventPublisher eventPublisher;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenPair authenticateUser(String loginId, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginId, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return generateTokenPair(userPrincipal);
    }

    public TokenPair generateTokenPair(UserPrincipal userPrincipal) {
        String accessToken = tokenProvider.generateAccessToken(userPrincipal);
        String refreshToken = tokenProvider.generateRefreshToken(userPrincipal);

        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + userPrincipal.getUsername(),
                refreshToken,
                tokenProvider.getRefreshTokenExpiration(),
                TimeUnit.MICROSECONDS
        );

        return new TokenPair(accessToken, refreshToken);
    }

    public TokenPair refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new ApiException(JwtTokenErrorCode.INVALID_REFRESH_TOKEN);
        }

        String loginId = tokenProvider.getUserLoginIdFromToken(refreshToken);
        String savedRefreshToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + loginId);

        if (!refreshToken.equals(savedRefreshToken)) {
            throw new ApiException(JwtTokenErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        return generateTokenPair(userPrincipal);
    }

    @Transactional
    public UserResponseDto registerUser(UserCreateRequestDto requestDto) {

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new ApiException(UserErrorCode.DUPLICATE_EMAIL);
        }

        if (userRepository.existsByLoginId(requestDto.getLoginId())) {
            throw new ApiException(UserErrorCode.DUPLICATE_LOGIN_ID);
        }

        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        User user = User.createUser(requestDto);

        validationUtils.validate(user);

        user = userRepository.save(user);
        // 이벤트 발생
        eventPublisher.publish(new UserCreatedEvent(user));

        return UserResponseDto.toResponse(user);
    }
}
