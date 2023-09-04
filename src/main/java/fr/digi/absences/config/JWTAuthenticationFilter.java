package fr.digi.absences.config;

import fr.digi.absences.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Configuration
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        if (req.getCookies() != null) {
            Stream.of(req.getCookies())
                    .filter(cookie -> cookie.getName().equals(jwtService.getCookie()))
                    .map(Cookie::getValue)
                    .forEach(token -> {
                        Claims body = Jwts
                                .parserBuilder()
                                .setSigningKey(jwtService.getSecretKey())
                                .build()
                                .parseClaimsJws(token)
                                .getBody();

                        String username = body.getSubject();

                        List<String> roles = body.get("roles", List.class);

                        List<SimpleGrantedAuthority> authorities = roles
                                .stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());
                        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
        }
        chain.doFilter(req, res);
    }
}
