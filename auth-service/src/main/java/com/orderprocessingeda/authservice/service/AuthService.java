package com.orderprocessingeda.authservice.service;

import com.orderprocessingeda.authservice.dto.AuthResponse;
import com.orderprocessingeda.authservice.dto.LoginRequest;
import com.orderprocessingeda.authservice.dto.RegisterRequest;
import com.orderprocessingeda.authservice.entity.User;
import com.orderprocessingeda.authservice.jwt.JwtService;
import com.orderprocessingeda.authservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request){

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request){

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(), user.getPassword()
        );

        if(!passwordMatches){
            throw new RuntimeException(
                    "Invalid Credentials"
            );
        }

        String token = jwtService.generateToken(request.getUsername());

        return new AuthResponse(token);
    }
}
