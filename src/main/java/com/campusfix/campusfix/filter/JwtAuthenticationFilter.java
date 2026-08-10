package com.campusfix.campusfix.filter;

import com.campusfix.campusfix.service.CustomUserDetailsService;
import com.campusfix.campusfix.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = extractToken(request);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String username;

        try {
            username = jwtService.extractUsername(jwt);
        } catch (Exception exception) {
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(username);

            try {

                jwtService.extractAllClaims(jwt);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

            } catch (Exception exception) {
                // Invalid or expired JWT.
                // Continue without authentication.
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(
            HttpServletRequest request) {

        /*
         * First check the Authorization header.
         *
         * This keeps the existing REST/Postman
         * Bearer-token authentication working.
         */
        String authHeader =
                request.getHeader("Authorization");

        if (authHeader != null &&
                authHeader.startsWith("Bearer ")) {

            return authHeader.substring(7);
        }

        /*
         * If there is no Authorization header,
         * check the browser cookie.
         */
        Cookie[] cookies =
                request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {

                if ("CAMPUSFIX_JWT"
                        .equals(cookie.getName())) {

                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}