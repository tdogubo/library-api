package com.etz.libraryapi.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String isbn;
    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String publisher;

    @Column
    private String genre;

    @Column
    private LocalDate publicationDate;

    @Column
    private String description;

    @Column
    private String language;

    @Column
    private int pages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalogue_id")
    private Catalog catalog;

    @ManyToMany(mappedBy = "books")
    private List<Author> authors;

}
