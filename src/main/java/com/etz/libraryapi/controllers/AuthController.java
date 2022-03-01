package com.etz.libraryapi.controllers;

import com.etz.libraryapi.domains.requests.CreateUserRequest;
import com.etz.libraryapi.domains.requests.LoginUserRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.UserResponse;
import com.etz.libraryapi.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AppResponse<UserResponse>> register(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AppResponse<UserResponse>> login(@Valid @RequestBody LoginUserRequest request, HttpServletResponse cookieResponse) {
        return userService.loginUser(request, cookieResponse);
    }
}
