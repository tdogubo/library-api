package com.etz.libraryapi.domains.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class BookRequest {
    @NotNull(message = "Required field")
    @NotBlank(message = "Required field")
    private String title;
    @NotNull(message = "Required field")
    @NotBlank(message = "Required field")
    private String firstName;
    @NotNull(message = "Required field")
    @NotBlank(message = "Required field")
    private String lastName;
}
