package co.com.pragma.security;

import co.com.pragma.model.user.User;
import co.com.pragma.model.util.JwtGateway;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
@NoArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProvider implements JwtGateway {
    private Long expiration;

    SecretKey key = Jwts.SIG.HS512.key().build();


    @Override
    public String generateToken(User user, String role) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(user.getEmail()) // Identificador único
                .claim("userId", user.getUserId())
                .claim("name", user.getName() + " " + user.getLastName()) // Nombre completo
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expiration)))
                .signWith(key)
                .compact();
    }

    public Claims validateTokenAndGetClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }
}
