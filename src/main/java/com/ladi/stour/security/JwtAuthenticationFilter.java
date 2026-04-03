package com.ladi.stour.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_COOKIE_NAME = "token";
    private static final List<String> FALLBACK_TOKEN_COOKIE_NAMES = List.of(
            TOKEN_COOKIE_NAME,
            "jwt",
            "accessToken",
            "access_token",
            "authToken"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        String method = request.getMethod();
        boolean publicRequest = isPublicEndpoint(requestPath, method);

        if ("OPTIONS".equalsIgnoreCase(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = extractTokenFromRequest(request);

            if (token == null) {
                if (publicRequest) {
                    filterChain.doFilter(request, response);
                    return;
                }
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Missing authorization token");
                return;
            }

            if (!jwtTokenProvider.validateToken(token)) {
                if (publicRequest) {
                    filterChain.doFilter(request, response);
                    return;
                }
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }

            String userId = jwtTokenProvider.getUserIdFromToken(token);
            String username = jwtTokenProvider.getUsernameFromToken(token);

            request.setAttribute("userId", userId);
            request.setAttribute("username", username);
            setAuthentication(request, userId, username);
        } catch (Exception e) {
            if (publicRequest) {
                filterChain.doFilter(request, response);
                return;
            }
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: " + e.getMessage());
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }

        String tokenFromCookie = extractTokenFromCookies(request);
        if (tokenFromCookie != null && !tokenFromCookie.isBlank()) {
            return tokenFromCookie;
        }
        return null;
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }

        for (String cookieName : FALLBACK_TOKEN_COOKIE_NAMES) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isBlank()) {
                    return normalizeToken(cookie.getValue());
                }
            }
        }

        return null;
    }

    private String normalizeToken(String token) {
        if (token == null) {
            return null;
        }

        String trimmedToken = token.trim();
        if (trimmedToken.startsWith(BEARER_PREFIX)) {
            return trimmedToken.substring(BEARER_PREFIX.length());
        }

        return trimmedToken;
    }

    private void setAuthentication(HttpServletRequest request, String userId, String username) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean isPublicEndpoint(String requestPath, String method) {
        if (requestPath.startsWith("/swagger-ui") ||
            requestPath.startsWith("/v3/api-docs") ||
            requestPath.startsWith("/api-docs") ||
            requestPath.equals("/api-docs") ||
            requestPath.startsWith("/webjars")) {
            return true;
        }

        if (requestPath.startsWith("/api/auth/login") || requestPath.startsWith("/api/auth/register")) {
            return true;
        }

        if ("GET".equalsIgnoreCase(method)) {
            return requestPath.startsWith("/api/categories") ||
                   requestPath.startsWith("/api/destinations") ||
                   requestPath.startsWith("/api/posts") ||
                   requestPath.startsWith("/api/tours") ||
                   requestPath.startsWith("/api/reviews") ||
                   requestPath.startsWith("/api/settings");
        }

        return false;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        response.getWriter().flush();
    }
}
