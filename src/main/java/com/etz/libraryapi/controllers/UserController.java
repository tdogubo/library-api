package com.etz.libraryapi.controllers;

import com.etz.libraryapi.domains.requests.CreateUserRequest;
import com.etz.libraryapi.domains.requests.EditUserRequest;
import com.etz.libraryapi.domains.requests.LoginUserRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.CreateUserResponse;
import com.etz.libraryapi.domains.responses.EditUserResponse;
import com.etz.libraryapi.domains.responses.LoginUserResponse;
import com.etz.libraryapi.models.User;
import com.etz.libraryapi.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<AppResponse<CreateUserResponse>> register(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }
    @PostMapping("/auth/login")
    public ResponseEntity<AppResponse<LoginUserResponse>> login(@Valid @RequestBody LoginUserRequest request, HttpServletResponse cookieResponse) {
        return userService.loginUser(request, cookieResponse);
    }
    @PutMapping("/users/{id}")
    public ResponseEntity<AppResponse<EditUserResponse>> editUser(@PathVariable("id") UUID id, @RequestBody EditUserRequest request) {
         return userService.editUser(id, request);
    }

}
