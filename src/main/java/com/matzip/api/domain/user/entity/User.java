package com.matzip.api.domain.user.entity;

import com.matzip.api.common.entity.BaseTimeEntity;
import com.matzip.api.domain.recommendation.entity.UserPreference;
import com.matzip.api.domain.review.entity.Review;
import com.matzip.api.domain.user.dto.UserCreateRequestDto;
import com.matzip.api.domain.user.enums.AuthProvider;
import com.matzip.api.domain.user.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    @Column
    private String providerId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private UserPreference preference;

    @Builder
    public User(Long id, String username, String email, String password, String name, Boolean emailVerified,
                AuthProvider provider, String providerId) {
        this.id = id;
        this.loginId = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.emailVerified = emailVerified;
        this.provider = provider;
        this.providerId = providerId;
    }

    public static User createUser(UserCreateRequestDto requestDto) {
        return User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .name(requestDto.getName())
                .emailVerified(requestDto.getEmailVerified())
                .provider(requestDto.getProvider())
                .providerId(requestDto.getProviderId())
                .build();
    }
}
