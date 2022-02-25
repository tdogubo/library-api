package com.etz.libraryapi.domains.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
public class EditUserRequest {
    private String password;

    private String address;

    private String phoneNumber;
}
