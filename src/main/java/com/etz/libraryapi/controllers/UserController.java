package com.etz.libraryapi.controllers;

import com.etz.libraryapi.domains.requests.EditUserRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.UserResponse;
import com.etz.libraryapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse<UserResponse>> editUser(@PathVariable("id") UUID id, @RequestBody EditUserRequest request) {
        return userService.editUser(id, request);
    }
}
