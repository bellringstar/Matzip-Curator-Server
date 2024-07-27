package com.matzip.api.domain.review.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.matzip.api.domain.review.entity.vo.ReviewAuthor;
import com.matzip.api.domain.user.entity.User;
import com.matzip.api.domain.user.entity.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class ReviewAuthorFactoryTest {

    ReviewAuthorFactory factory;

    @Mock
    User user;

    @BeforeEach
    void setUp() {
        factory = new ReviewAuthorFactory();
    }

    @Test
    @DisplayName("유효한 Authentication with UserPrincipal이 주어졌을 때, createFromSecurityContext 메서드는 ReviewAuthor를 성공적으로 생성한다.")
    public void test_create_from_valid_authentication() {
        when(user.getId()).thenReturn(1L);

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);

        // When
        ReviewAuthor reviewAuthor = factory.createFromSecurityContext(authentication);

        // Then
        assertNotNull(reviewAuthor);
        assertNotNull(reviewAuthor.getDisplayName());
        assertEquals(1L, reviewAuthor.getId());
    }

    @Test
    @DisplayName("Authentication의 principal이 UserPrincipal의 인스턴스가 아니라면, createFromSecurityContext는 IllegalArgumentException을 발생시킨다.")
    public void test_authentication_principal_not_instance_of_userprincipal() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new Object());

        ReviewAuthorFactory factory = new ReviewAuthorFactory();

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> factory.createFromSecurityContext(authentication));
        assertEquals("Unexpected principal type", exception.getMessage());
    }

    @Test
    @DisplayName("Authentication principal이 null 일 때, createFromSecurityContext는 IllegalArgumentException을 발생시킨다.")
    public void test_authentication_principal_is_null() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(null);

        ReviewAuthorFactory factory = new ReviewAuthorFactory();

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> factory.createFromSecurityContext(authentication));
        assertEquals("Unexpected principal type", exception.getMessage());
    }
}