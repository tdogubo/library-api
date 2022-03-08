package com.etz.libraryapi.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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
    private Set<Author> authors = new HashSet<>();

    private String generateCallNumber(){
        assert this.title != null;
        String title = this.title.toUpperCase();
        assert this.publicationDate != null;
        int year = this.publicationDate.getYear();
        assert this.catalog != null;
        String catalog = this.catalog.getName().toUpperCase();

        return title.substring(0,1) + year + catalog.charAt(0);

    }

    public void addAuthor(Author author){
        authors.add(author);
    }

}
