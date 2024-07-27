package com.matzip.api.security.dto;

import com.matzip.api.domain.user.enums.AuthProvider;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserCreateRequestDto {

    @NotNull
    private String loginId;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String name;

    private Boolean emailVerified = false;

    private AuthProvider provider;

    private String providerId;
}
