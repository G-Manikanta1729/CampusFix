package com.campusfix.campusfix.service;

import com.campusfix.campusfix.dto.AuthenticationRequest;
import com.campusfix.campusfix.dto.AuthenticationResponse;
import com.campusfix.campusfix.model.User;
import com.campusfix.campusfix.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse authenticate(
            AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow();

        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRole()
        );

        return new AuthenticationResponse(token);
    }
}