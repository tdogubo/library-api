package com.etz.libraryapi.domains.responses;

import com.etz.libraryapi.models.Author;
import com.etz.libraryapi.models.Catalog;

import java.time.LocalDate;
import java.util.List;

public class BookResponse {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String genre;
    private LocalDate publicationDate;
    private String description;
    private String language;
    private int pages;
    private Catalog catalog;
    private List<Author> authors;
}
