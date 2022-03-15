package com.etz.libraryapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "catalog")
    @JsonIgnore
    private List<Book> books;

    private ArrayList<String> bookList = new ArrayList<>();

    public ArrayList<String> getCatalogBooks(){
        for (Book book: books) {
            String title = book.getTitle();
            bookList.add(title);
        }
        return bookList;
    }
}
