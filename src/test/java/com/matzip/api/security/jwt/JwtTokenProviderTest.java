package com.matzip.api.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.matzip.api.domain.user.entity.User;
import com.matzip.api.domain.user.entity.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("JwtTokenProvider 클래스")
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserPrincipal userPrincipal;

    @Mock
    private User user;

    private String privateKeyStr = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCqS3uLLdafAm5C5dnkmbmVg6fxUJwwroftwMt3/R7p15MDx4X1X+zjK4G5nRavIIcpKYRM3StIHXuPuHgybQ1gtwrLor74L9zEbbXfgSQhzc2c7xmmQlflTHSbu7fvAyLeraw0T4D9XC5c6TO557h+WrSntXjPdno5jKSThiKXjgojeTxYG1KAXqV6g3eB9xrvh+VgbdTJuOkik65ehn53X9UW/IAXdyIWo0Eligz8/JQXbwDC5aGJOsOY6Hcwf+lbgc42Emx4JX4QFCSoPoPs/dIbkkET7DVS1p/vWgG5wMVyLe7YJyu0J/oDVCnzL0c4DonveDg92P68T91WRRJZAgMBAAECggEAE/tWKF01LhR9D1q0y9zpUKTBfaBjqVcrUi+ReFdyK4Zc5OBCJMnwC/JQjxDihuNKVL8/O70mXVh/RXyD1wfiflcP/Few8IJ+NSq54NtAe/pv7kKEeBSYB2rmkljvEsujiW0ns/4hsKjitEp1RDKvPF1EjhquLaq1WgAZlglS0QU7eXG8ZtRHY84bfzoM32dShn/OtO+q9Ks20Ysjr85LyQEfarUFD2KnvfLv1PwqfJr6+t7u4NJKlHLZ4d83JarZLlcPyB3/4B6OLozr5JzFdXScRkra9LNoy+8DnlfiH/QgzC9xaTNg3MTmzlL9EWLwsYttIFvLj7Wjsjh898HN4QKBgQC7AG1fy8E9lyhwOZ+282jx1EZXPUZREDGLzL3JgJsvwSmJYwfOHhXFJ7PS4BUd5gATsGE1ErF1v7Yh4xRfkd4n5zPMEGKQnrOD20LH4t5ePnMcSIqDXS/3j+VFXDsy3QJiHDm6yOrLXZTR94f09z7fuKwCKiwnOC6X4xFWnXN7eQKBgQDpIPvP8hPbOVm4DaZZQPPoOIlX8umH+6edfoSYVGFC1449Sze2bx8rPEWDzfgELstYY1yNgW7rbKgN5JTvcjBsUeuFyvX9XFVZl7dkvBC+JRZMUDCiZraqSRfgphInkO/Zcbw0mqFbXEmwp5Im4aXKCs9Y5G44mJljpzkm4gG14QKBgGfubNt5GwVoJYl5/VJpJkKBgDasT5BuN5tDR5feamG4MaWx7HyaK+9PiZoCALqFQjWvKdeddBPFYSykUIZtQ9NZpGgeEyndilgWXJ9Lb9thSgjGKHWqLSaiSwKpqB00yEZO+abeU+CDOlsX//AeGkutD6yEU9XYW8z+3nwNNknBAoGAAzywAm0sFWkwc7vMyYR7XogacBwxXfACnc857BuP1ivED9nRhjj4x9LobDIZ9YTo0etfR5+6eI5jrv2zi7ecMITarlrpG4GCteHDbCSoXtuI+bpLUpX2h1rIeBgWPHJowi4wHNLg1rHrXAaXou6TLMEFrAhP93029AlIY9/TgkECgYA9/dQHB8d02FebqWTA2K5Fhg8IfXg9SKwhDUZnPxOwlp0M8sUDlyh0xlo/P0DZ/AwebW3Zf96/Yuj11tx3xKW8z5s20d6rDKd4s89f9mp2vWap9CnnFxJrPW7NDowAdMzUATK04k9DE+Sel1FvsvArXvMHYAm8WAazey04eqU6WQ==";
    private String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqkt7iy3WnwJuQuXZ5Jm5lYOn8VCcMK6H7cDLd/0e6deTA8eF9V/s4yuBuZ0WryCHKSmETN0rSB17j7h4Mm0NYLcKy6K++C/cxG2134EkIc3NnO8ZpkJX5Ux0m7u37wMi3q2sNE+A/VwuXOkzuee4flq0p7V4z3Z6OYykk4Yil44KI3k8WBtSgF6leoN3gfca74flYG3UybjpIpOuXoZ+d1/VFvyAF3ciFqNBJYoM/PyUF28AwuWhiTrDmOh3MH/pW4HONhJseCV+EBQkqD6D7P3SG5JBE+w1Utaf71oBucDFci3u2CcrtCf6A1Qp8y9HOA6J73g4Pdj+vE/dVkUSWQIDAQAB";

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        ReflectionTestUtils.setField(jwtTokenProvider, "privateKey", privateKey);
        ReflectionTestUtils.setField(jwtTokenProvider, "publicKey", publicKey);
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenExpiration", 3600000L); // 1 hour
        ReflectionTestUtils.setField(jwtTokenProvider, "refreshTokenExpiration", 86400000L); // 1 day

        when(userPrincipal.getUser()).thenReturn(user);
        when(user.getLoginId()).thenReturn("testuser");
    }

    @Nested
    @DisplayName("generateAccessToken 메서드는")
    class Describe_generateAccessToken {

        @Test
        @DisplayName("유효한 액세스 토큰을 생성한다")
        void it_generates_valid_access_token() {
            String token = jwtTokenProvider.generateAccessToken(userPrincipal);
            assertThat(token).isNotNull();
            Claims claims = Jwts.parserBuilder().setSigningKey(jwtTokenProvider.getPublicKey()).build()
                    .parseClaimsJws(token).getBody();
            assertThat(claims.getSubject()).isEqualTo("testuser");
        }
    }

    @Nested
    @DisplayName("generateRefreshToken 메서드는")
    class Describe_generateRefreshToken {

        @Test
        @DisplayName("유효한 리프레시 토큰을 생성한다")
        void it_generates_valid_refresh_token() {
            String token = jwtTokenProvider.generateRefreshToken(userPrincipal);
            assertThat(token).isNotNull();
            Claims claims = Jwts.parserBuilder().setSigningKey(jwtTokenProvider.getPublicKey()).build()
                    .parseClaimsJws(token).getBody();
            assertThat(claims.getSubject()).isEqualTo("testuser");
        }
    }

    @Nested
    @DisplayName("getUserLoginIdFromToken 메서드는")
    class Describe_getUserLoginIdFromToken {

        @Test
        @DisplayName("토큰에서 사용자 LoginId를 추출한다")
        void it_extracts_user_login_id_from_token() {
            String token = jwtTokenProvider.generateAccessToken(userPrincipal);
            String loginId = jwtTokenProvider.getUserLoginIdFromToken(token);
            assertThat(loginId).isEqualTo("testuser");
        }
    }

    @Nested
    @DisplayName("validateToken 메서드는")
    class Describe_validateToken {

        @Test
        @DisplayName("유효한 토큰일 경우 true를 반환한다")
        void it_returns_true_for_valid_token() {
            String token = jwtTokenProvider.generateAccessToken(userPrincipal);
            boolean isValid = jwtTokenProvider.validateToken(token);
            assertThat(isValid).isTrue();
        }

        @Test
        @DisplayName("유효하지 않은 토큰일 경우 false를 반환한다")
        void it_returns_false_for_invalid_token() {
            String invalidToken = "invalidToken";
            boolean isValid = jwtTokenProvider.validateToken(invalidToken);
            assertThat(isValid).isFalse();
        }
    }
}
