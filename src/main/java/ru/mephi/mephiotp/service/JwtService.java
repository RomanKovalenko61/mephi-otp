package ru.mephi.mephiotp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.mephi.mephiotp.model.User;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    @Value("${jwt.signing.key}")
    private String jwtSigningKey;

    @Value("${jwt.expiration.time}")
    private Long expirationTime;

    public String generateToken(User user) {
        Key key = Keys.hmacShaKeyFor(jwtSigningKey.getBytes());
        Date now = new Date(System.currentTimeMillis());
        Date expiration = new Date(now.getTime() + expirationTime);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(jwtSigningKey.getBytes());
            Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSigningKey.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSigningKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        String role = claims.get("role", String.class);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(username, token, authorities);
    }
}
