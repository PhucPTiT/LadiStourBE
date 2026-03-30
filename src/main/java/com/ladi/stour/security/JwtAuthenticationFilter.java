package com.ladi.stour.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * List of endpoints that don't require authentication
     */
    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/register",
            "/api/settings",
            "/api/categories",
            "/api/destinations",
            "/api/tours",
            "/api/reviews"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        String method = request.getMethod();

        // Skip authentication for public endpoints and GET requests on certain resources
        if (isPublicEndpoint(requestPath, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = extractTokenFromRequest(request);

            if (token == null) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Missing authorization token");
                return;
            }

            if (!jwtTokenProvider.validateToken(token)) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }

            // Token is valid, set user info in request for controllers to access
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            String username = jwtTokenProvider.getUsernameFromToken(token);

            request.setAttribute("userId", userId);
            request.setAttribute("username", username);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: " + e.getMessage());
        }
    }

    /**
     * Extract JWT token from Authorization header
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }

        return authHeader.substring(BEARER_PREFIX.length());
    }

    /**
     * Check if request is to a public endpoint
     */
    private boolean isPublicEndpoint(String requestPath, String method) {
        // Allow GET requests for read operations
        if ("GET".equals(method)) {
            return requestPath.startsWith("/api/categories") ||
                   requestPath.startsWith("/api/destinations") ||
                   requestPath.startsWith("/api/posts") ||
                   requestPath.startsWith("/api/tours") ||
                   requestPath.startsWith("/api/reviews") ||
                   requestPath.startsWith("/api/settings/default");
        }

        // Check if path is in public endpoints list
        for (String publicEndpoint : PUBLIC_ENDPOINTS) {
            if (requestPath.startsWith(publicEndpoint) &&
                ("POST".equals(method) || "GET".equals(method))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Send error response
     */
    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        response.getWriter().flush();
    }
}
