package com.campusfix.campusfix.controller;

import com.campusfix.campusfix.dto.AuthenticationRequest;
import com.campusfix.campusfix.dto.AuthenticationResponse;
import com.campusfix.campusfix.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(
            AuthenticationService authenticationService) {

        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(
            @RequestBody @Valid AuthenticationRequest request,
            jakarta.servlet.http.HttpServletResponse response) {

        AuthenticationResponse authenticationResponse =
                authenticationService.authenticate(request);

        ResponseCookie cookie =
                ResponseCookie.from(
                                "CAMPUSFIX_JWT",
                                authenticationResponse.getToken()
                        )
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .maxAge(60 * 60)
                        .sameSite("Lax")
                        .build();

        response.setHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );

        return authenticationResponse;
    }
}