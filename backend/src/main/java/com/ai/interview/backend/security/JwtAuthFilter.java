package com.ai.interview.backend.security;

import com.ai.interview.backend.entity.User;
import com.ai.interview.backend.repository.UserRepository;
import com.ai.interview.backend.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. read authorization header
        String authorizationHeader = request.getHeader("Authorization");

        // 2. if no JWT, continue without authentication
    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
    }

    // 3. extract token (remove "Bearer ")
        String token = authorizationHeader.substring(7);

    try {
        // 4. extract email from JWT
        String email = jwtService.extractEmail(token);

        // 5. Load user from DB
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user.getEmail(),
                            null, List.of()
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authToken);
        }
    }  catch (Exception e) {
        SecurityContextHolder.clearContext();
    }

        // 7. continue filter chain
        filterChain.doFilter(request, response);

    }
}

//Why this class exists:
//
//Runs before controllers
//
//Validates JWT
//
//Tells Spring Security:
//
//        “This request is authenticated”
//
//         This is why it MUST be in security.
