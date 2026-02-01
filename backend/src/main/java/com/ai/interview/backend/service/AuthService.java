package com.ai.interview.backend.service;

import com.ai.interview.backend.DTO.LoginRequest;
import com.ai.interview.backend.DTO.RegisterRequest;
import com.ai.interview.backend.entity.User;
import com.ai.interview.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // Registers a user if email is already used then error
    public void register(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
    }

    // logs in if the request's user is valid, which means valid email and password
    public User login(LoginRequest request) {
        User user = new User();

        user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (! passwordEncoder.matches(request.getPassword(), user.getPassword()) )
            throw new BadCredentialsException("Invalid email or password");

        return user;

    }
}
