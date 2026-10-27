
package com.robinhood.user.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Skip JWT processing for public endpoints
    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request) {

        String path = request.getServletPath();
        String method = request.getMethod();

        // Public health endpoint
        if ("GET".equals(method) &&
                (path.equals("/actuator/health")
                || path.startsWith("/actuator/health/"))) {
            return true;
        }

        // Public authentication endpoints
        if ("POST".equals(method) &&
                (path.equals("/api/v1/auth/login")
                || path.equals("/api/v1/auth/register"))) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Check for JWT Bearer token
        if (authHeader != null &&
                authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            // Validate JWT token
            if (jwtUtil.isValid(token)) {

                String userId = jwtUtil.extractUserId(token);
                String role = jwtUtil.extractRole(token);

                // Set authenticated user details
                var authentication =
                    new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        List.of(
                            new SimpleGrantedAuthority(
                                "ROLE_" + role
                            )
                        )
                    );

                SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
            }
        }

        // Continue request processing
        filterChain.doFilter(request, response);
    }
}
