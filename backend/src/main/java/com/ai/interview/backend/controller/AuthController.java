package com.ai.interview.backend.controller;

import com.ai.interview.backend.DTO.LoginRequest;
import com.ai.interview.backend.DTO.RegisterRequest;
import com.ai.interview.backend.entity.User;
import com.ai.interview.backend.service.AuthService;
import com.ai.interview.backend.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;
    private final JWTService jWTService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = authService.login(request);
        String token = jWTService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
