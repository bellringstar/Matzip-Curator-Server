package com.matzip.api.security.service;

import com.matzip.api.domain.user.entity.User;
import com.matzip.api.domain.user.entity.UserPrincipal;
import com.matzip.api.domain.user.enums.AuthProvider;
import com.matzip.api.domain.user.repository.UserRepository;
import com.matzip.api.security.oauth2.user.OAuth2UserInfo;
import com.matzip.api.security.oauth2.user.OAuth2UserInfoFactory;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes()
        );

        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail())
                .map(existingUser -> updateExistingUser(existingUser, userRequest, oAuth2UserInfo))
                .orElseGet(() -> registerNewUser(userRequest, oAuth2UserInfo));

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest userRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = User.builder()
                .loginId(oAuth2UserInfo.getEmail())
                .email(oAuth2UserInfo.getEmail())
                .password(UUID.randomUUID().toString())
                .name(oAuth2UserInfo.getName())
                .provider(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase()))
                .providerId(oAuth2UserInfo.getId())
                .build();
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser,OAuth2UserRequest userRequest, OAuth2UserInfo oAuth2UserInfo) {
        return existingUser.updateOAuth(userRequest, oAuth2UserInfo);
    }
}
