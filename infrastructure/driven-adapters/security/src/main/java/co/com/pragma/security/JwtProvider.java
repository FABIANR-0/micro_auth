package co.com.pragma.security;

import co.com.pragma.model.user.User;
import co.com.pragma.model.util.JwtGateway;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
@NoArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProvider implements JwtGateway {
    private String secret;

    private Long expiration;

    private Key key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(User user, String role) {
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(user.getEmail()) // Identificador único
                .claim("userId", user.getUserId())
                .claim("name", user.getName() + " " + user.getLastName()) // Nombre completo
                .claim("role", role)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(expiration)))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
}
