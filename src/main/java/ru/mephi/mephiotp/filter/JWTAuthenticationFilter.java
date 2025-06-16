package ru.mephi.mephiotp.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.mephi.mephiotp.service.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                if (jwtService.validateToken(token)) {
                    Authentication authentication = jwtService.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("JWT token is valid, user authenticated: {}", authentication.getName());
                }
            } catch (JwtException e) {
                request.setAttribute("jwt_exception", e);
                log.error("JWT error: {}", e.getMessage());
                //throw new AuthenticationServiceException("JWT error", e);
            }
        }
        filterChain.doFilter(request, response);
    }
}
