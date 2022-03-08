package com.etz.libraryapi.domains.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class AddBookResponse {
    private Long id;
    private String isbn;
    private String title;
    private List<String> authors;
    private String publisher;
    private String genre;
    private LocalDate publicationDate;
    private String description;
    private String language;
    private int pages;
}
