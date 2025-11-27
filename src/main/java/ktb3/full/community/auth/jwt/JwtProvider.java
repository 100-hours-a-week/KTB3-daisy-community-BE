package ktb3.full.community.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import ktb3.full.community.user.domain.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.time.Instant;
import java.util.Date;


@Component
public class JwtProvider {
    private final Key key;
    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtProvider(
            @Value("${auth.jwt.secret}") String secret,
            @Value("${auth.jwt.access-token-expiration}")long accessExpiration,
            @Value("${auth.jwt.refresh-token-expiration}")long refreshExpiration) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessExpiration = accessExpiration * 1000L;
        this.refreshExpiration = refreshExpiration * 1000L;
    }

    private static Date toDate(Instant instant) {
        return Date.from(instant);
    }

    public String createAccessToken(Long userId, String email, Role role) {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(accessExpiration);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role.name())
                .setIssuedAt(toDate(now))
                .setExpiration(toDate(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Long userId) {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(refreshExpiration);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(toDate(now))
                .setExpiration(toDate(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public Long getUserId(String token) {
        return Long.valueOf(getClaims(token).getBody().getSubject());
    }

    public String getRole(String token) {
        return getClaims(token).getBody().get("role", String.class);
    }

    public Instant getExpiration(String token) {
        return getClaims(token).getBody().getExpiration().toInstant();
    }
}