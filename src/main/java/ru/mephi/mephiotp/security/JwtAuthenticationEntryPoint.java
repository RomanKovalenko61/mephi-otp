package ru.mephi.mephiotp.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        String message = "Ошибка аутентификации";

        Throwable cause = (Throwable) request.getAttribute("jwt_exception");
        if (cause == null) {
            cause = authException.getCause();
        }
        if (cause instanceof ExpiredJwtException) {
            message = "Срок действия токена истёк";
        } else if (cause instanceof JwtException) {
            message = "Некорректный токен";
        }
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}