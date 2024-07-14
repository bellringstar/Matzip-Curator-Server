package com.matzip.api.security.jwt;

import com.matzip.api.domain.user.entity.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.privateKey}")
    private String privateKeyStr;

    @Value("${jwt.publicKey}")
    private String publicKeyStr;

    @Value("${jwt.accessExpiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refreshExpiration}")
    private long refreshTokenExpiration;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    private void init() throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] privateKeyBytes = Decoders.BASE64.decode(privateKeyStr);
        byte[] publicKeyBytes = Decoders.BASE64.decode(publicKeyStr);
        privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    }

    public String generateAccessToken(UserPrincipal userPrincipal) {
        return generateToken(userPrincipal, accessTokenExpiration);
    }

    public String generateRefreshToken(UserPrincipal userPrincipal) {
        return generateToken(userPrincipal, refreshTokenExpiration);
    }

    private String generateToken(UserPrincipal userPrincipal, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userPrincipal.getUser().getLoginId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public String getUserLoginIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token", e);
        }
        return false;
    }
}
