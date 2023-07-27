package fr.digi.absences.service;

import fr.digi.absences.entity.Employee;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Configuration
@Data
@Service
public class JwtService {
    @Value("${jwt.expires_in}")
    private long expireIn;

    @Value("${jwt.cookie}")
    private String cookie;

    @Value("${jwt.secret}")
    private String secret;

    private Key secretKey;

    @PostConstruct
    public void buildKey() {
        secretKey = new SecretKeySpec(Base64.getDecoder().decode(getSecret()),
                SignatureAlgorithm.HS256.getJcaName());
    }

    public String buildJWTCookie(Employee user) {
        String jetonJWT = Jwts.builder()
                .setSubject(user.getEmail())
                .addClaims(Map.of("roles", user.getRoles()))
                .setExpiration(new Date(System.currentTimeMillis() + getExpireIn() * 1000))
                .signWith(
                        getSecretKey()
                ).compact();

        ResponseCookie tokenCookie = ResponseCookie.from(getCookie(), jetonJWT)
                .httpOnly(true)
                .maxAge(getExpireIn() * 1000)
                .path("/")
                .sameSite("Lax")
                .build();

        return tokenCookie.toString();
    }
}
