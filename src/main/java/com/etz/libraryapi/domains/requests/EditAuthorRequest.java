package com.etz.libraryapi.domains.requests;

import com.etz.libraryapi.models.Book;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class EditAuthorRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String newName;
    private Set<Book> books = new HashSet<>();
}
