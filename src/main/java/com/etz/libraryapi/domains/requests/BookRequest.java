package com.etz.libraryapi.domains.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class BookRequest {
    @NotNull(message = "Required field")
    @NotBlank(message = "Required field")
    private String title;
    @NotEmpty(message = "Required field")
    private List<String> authors;
}
