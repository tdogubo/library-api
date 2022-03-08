package com.etz.libraryapi.domains.responses;

import com.etz.libraryapi.models.Book;

import java.util.List;

public class CatalogResponse {
    private Long id;

    private String name;

    private List<Book> books;
}
