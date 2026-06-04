package com.orderprocessingeda.authservice.controller;

import com.orderprocessingeda.authservice.dto.AuthResponse;
import com.orderprocessingeda.authservice.dto.LoginRequest;
import com.orderprocessingeda.authservice.dto.RegisterRequest;
import com.orderprocessingeda.authservice.jwt.JwtService;
import com.orderprocessingeda.authservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;
    private JwtService jwtService;
   // private RegisterRequest registerRequest;

    public AuthController(AuthService authService, JwtService jwtService){
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));

    }

    @GetMapping("/test")
    public String test() {

        String token = jwtService.generateToken("atul");
        return jwtService.extractUsername(token);

    }
}
