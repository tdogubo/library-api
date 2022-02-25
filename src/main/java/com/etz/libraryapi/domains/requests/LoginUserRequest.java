package com.etz.libraryapi.domains.requests;

import lombok.Data;

@Data
public class LoginUserRequest {
    private String email;
    private String password;
}
