package com.campusfix.campusfix.controller;

import com.campusfix.campusfix.dto.RegisterRequest;
import com.campusfix.campusfix.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.campusfix.campusfix.dto.UserResponse;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(
            @RequestBody @Valid RegisterRequest request) {

        return userService.register(request);
    }
}