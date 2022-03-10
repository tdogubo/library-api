package com.etz.libraryapi.domains.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BookResponse {
    private Long id;
    private String callNumber;
    private String isbn;
    private String title;
    private String publisher;
    private String genre;
    private String publishYear;
    private String description;
    private String language;
    private int pages;
    private List<String> authors;
    private int copies;
    private String catalog;

}
