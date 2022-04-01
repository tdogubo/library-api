package com.etz.libraryapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String callNumber;

    @Column
    private String isbn;
    @Column
    private String title;
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


    @OneToOne
    @JoinColumn(name = "history_id", referencedColumnName = "id")
    private BorrowHistory history;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinTable(name = "author_book",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    @JsonIgnore
    private Set<Author> authors = new HashSet<>();

    private ArrayList<String> authorList = new ArrayList<>();

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public ArrayList<String> getBookAuthors() {
        for (Author author : authors) {
            String authorFullName = author.getFirstName() + " " + author.getLastName();
            authorList.add(authorFullName);
        }
        return authorList;
    }


}
