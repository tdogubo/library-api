package com.etz.libraryapi.domains.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class AddBookRequest {
    private String isbn;
    private String title;
    private List<String> author;
    private String publisher;
    private String genre;
    private LocalDate publicationDate;
    private String description;
    private String language;
    private int pages;
}

