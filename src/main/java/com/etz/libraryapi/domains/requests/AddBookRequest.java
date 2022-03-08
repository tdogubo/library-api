package com.etz.libraryapi.domains.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class AddBookRequest {
    @NotBlank(message = "Required field")
    private String isbn;
    @NotBlank(message = "Required field")
    private String title;
    @NotEmpty(message = "Required field")
    private List<String> author;
    @NotBlank(message = "Required field")
    private String publisher;
    @NotBlank(message = "Required field")
    private String genre;
    @NotNull(message = "Required field")
    private LocalDate publicationDate;
    @NotBlank(message = "Required field")
    private String description;
    @NotBlank(message = "Required field")
    private String language;
    @NotNull(message = "Required field")
    private int pages;
}

