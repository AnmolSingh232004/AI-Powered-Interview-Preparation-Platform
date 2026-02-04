package com.ai.interview.backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @GetMapping("/me")
    public String me(Authentication authentication) {
        return "Hello " + authentication.getName();
    }
}

//Controller → handles HTTP
//
//Service → business logic
//
//JwtService → token logic
//
//JwtAuthFilter → request authentication
//
//SecurityConfig → wiring