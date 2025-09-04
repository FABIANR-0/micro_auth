package co.com.pragma.security;

import co.com.pragma.model.user.User;
import co.com.pragma.model.util.JwtGateway;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;

@Component
@NoArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProvider implements JwtGateway {

    private Long expiration;

    private String secret;

    private RSAKey rsaKey;

    @PostConstruct
    public void init() {
        try {
            // Generar par de llaves RSA
            this.rsaKey = new RSAKeyGenerator(2048)
                    .keyID(secret)
                    .algorithm(JWSAlgorithm.RS256)
                    .keyUse(KeyUse.SIGNATURE)
                    .generate();
        } catch (JOSEException e) {
            throw new RuntimeException("Error generating RSA key", e);
        }
    }

    @Override
    public String generateToken(User user, String role) {
        Instant now = Instant.now();

        try {
            RSAPrivateKey privateKey = rsaKey.toRSAPrivateKey();

            return Jwts.builder()
                    .subject(user.getEmail())
                    .claim("userId", user.getUserId())
                    .claim("name", user.getName() + " " + user.getLastName())
                    .claim("role", role)
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(now.plusSeconds(expiration)))
                    .signWith(privateKey)
                    .compact();
        } catch (JOSEException e) {
            throw new RuntimeException("Error signing token", e);
        }
    }

    public Claims validateTokenAndGetClaims(String token) {
        try {
            RSAPublicKey publicKey = rsaKey.toRSAPublicKey();

            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }
}
