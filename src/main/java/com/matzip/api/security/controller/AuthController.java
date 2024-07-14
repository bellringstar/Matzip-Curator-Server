package com.matzip.api.security.controller;

import com.matzip.api.common.api.Api;
import com.matzip.api.domain.user.dto.UserResponseDto;
import com.matzip.api.security.dto.LoginRequestDto;
import com.matzip.api.security.dto.RefreshTokenRequest;
import com.matzip.api.security.dto.UserCreateRequestDto;
import com.matzip.api.security.jwt.TokenPair;
import com.matzip.api.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public Api<UserResponseDto> registerUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        UserResponseDto userResponse = authService.registerUser(userCreateRequestDto);
        return Api.OK(userResponse);
    }

    @PostMapping("/signin")
    public Api<TokenPair> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        TokenPair tokenPair = authService.authenticateUser(loginRequestDto.getLoginId(), loginRequestDto.getPassword());
        return Api.OK(tokenPair);
    }

    @PostMapping("/refresh")
    public Api<TokenPair> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        TokenPair newTokenPair = authService.refreshToken(request.getRefreshToken());
        return Api.OK(newTokenPair);
    }
}
