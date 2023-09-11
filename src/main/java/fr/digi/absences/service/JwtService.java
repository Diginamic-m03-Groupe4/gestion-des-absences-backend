package fr.digi.absences.service;

import fr.digi.absences.consts.Roles;
import fr.digi.absences.dto.EmployeeDto;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.exception.UnauthorizedAcessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Configuration
@Data
@Service
@Slf4j
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


    public String buildJWTCookie(EmployeeDto user) {
        String jetonJWT = Jwts.builder()
                .setSubject(user.getEmail())
                .addClaims(Map.of("roles", user.getRole()))
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

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void verifyAuthorization(String token, Roles roles){
        Claims tokenClaims = extractAllClaims(token);
        tokenClaims.forEach((claim, what) -> log.info(claim));
        List<String> role = (List<String>) tokenClaims.get("roles");
        if(!role.contains(roles.getRole())){
            log.info(roles.getRole());
            throw new UnauthorizedAcessException("Accès non autorisé");
        }
    }

}
