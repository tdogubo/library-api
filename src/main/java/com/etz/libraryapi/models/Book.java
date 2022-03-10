package com.etz.libraryapi.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String isbn;
    @Column
    private String title;
    @Column
    private Boolean checkInStatus;
    @Column
    private Boolean checkOutStatus;
    @Column
    private String publisher;
    @Column
    private String genre;
    @Column
    private String publishYear;
    @Column
    private String description;
    @Column
    private String language;
    @Column
    private int pages;
    @Column
    private int copies;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalogue_id")
    private Catalog catalog;
    @Column
    private String callNumber;

    @ManyToMany(mappedBy = "books")
    private Set<Author> authors = new HashSet<>();

    public void addAuthor(Author author) {
        authors.add(author);
    }

}
