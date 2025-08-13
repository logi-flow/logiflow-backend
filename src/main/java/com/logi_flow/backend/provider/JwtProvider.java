package com.logi_flow.backend.provider;

import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.user.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    private final Key key;
    private final long jwtExpirationMs;
    private final long jwtResetPasswordExpirationMs;

    public long getExpirationMs() {
        return jwtExpirationMs;
    }

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long jwtExpirationMs,
            @Value("${jwt.reset-password-expiration-ms}") long jwtResetPasswordExpirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.jwtExpirationMs = jwtExpirationMs;
        this.jwtResetPasswordExpirationMs = jwtResetPasswordExpirationMs;
    }

    public String generateJwtToken(Long userId, UserRole role) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("userId", userId)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateResetPasswordJwtToken(String email) {
        return Jwts.builder()
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtResetPasswordExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String removeBearer(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException(ResponseMessage.INVALID_TOKEN);
        }

        return token.substring("Bearer ".length());
    }

    public void validateJwtToken(String token) {
        try {
            getClaims(token);
        } catch (ExpiredJwtException e) {
            throw new JwtException(ResponseMessage.TOKEN_EXPIRED, e);
        } catch (Exception e) {
            throw new JwtException(ResponseMessage.INVALID_TOKEN, e);
        }
    }

    public Claims getClaims(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public String getUsernameFromJwtToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("username", String.class);
    }

    public String getEmailFromJwtToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("email", String.class);
    }
}
