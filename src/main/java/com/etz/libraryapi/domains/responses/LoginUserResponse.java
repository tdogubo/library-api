package com.etz.libraryapi.domains.responses;

import lombok.Data;

import java.util.UUID;

@Data
public class LoginUserResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String status;
    private String address;
    private String phoneNumber;


}

