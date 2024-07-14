package com.matzip.api.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenPair {

    String accessToken;
    String refreshToken;
}
