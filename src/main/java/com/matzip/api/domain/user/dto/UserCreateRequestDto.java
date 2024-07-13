package com.matzip.api.domain.user.dto;

import com.matzip.api.domain.user.enums.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserCreateRequestDto {

    private String username;

    private String email;

    private String password;

    private String name;

    private Boolean emailVerified = false;

    private AuthProvider provider;

    private String providerId;
}
