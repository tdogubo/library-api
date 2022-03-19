package com.etz.libraryapi.services;

import com.etz.libraryapi.domains.requests.EditAuthorRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.models.Author;
import com.etz.libraryapi.models.Librarian;
import com.etz.libraryapi.repositories.AuthorRepo;
import com.etz.libraryapi.repositories.LibrarianRepo;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthorServiceTest {
    Author author = new Author();
    Librarian librarian = new Librarian();
    Faker faker = new Faker();
    @Autowired
    private AuthorService service;
    @MockBean
    private AuthorRepo authorRepo;
    @MockBean
    private LibrarianRepo librarianRepo;

    @BeforeEach
    void setUp() {
        author.setFirstName(faker.name().firstName());
        author.setLastName(faker.name().lastName());
        author.setId(UUID.randomUUID());


        librarian.setId(UUID.randomUUID());
    }

    @Test
    void canEditAuthor() {
        EditAuthorRequest request = new EditAuthorRequest();
        request.setFirstName(faker.name().firstName());
        request.setLastName(faker.name().lastName());

        when(authorRepo.findById(author.getId())).thenReturn(Optional.of(author));
        when(librarianRepo.findById(librarian.getId())).thenReturn(Optional.of(librarian));

        ResponseEntity<AppResponse<String>> response = service.editAuthor(librarian.getId(), author.getId(), request);

        //then
        assertEquals("", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

    }

    @Test
    void canNotEditAuthorMissingId() {
        EditAuthorRequest request = new EditAuthorRequest();
        request.setFirstName(faker.name().firstName());
        request.setLastName(faker.name().lastName());

        when(authorRepo.findById(author.getId())).thenReturn(Optional.empty());
        when(librarianRepo.findById(librarian.getId())).thenReturn(Optional.of(librarian));

        ResponseEntity<AppResponse<String>> response = service.editAuthor(librarian.getId(), author.getId(), request);

        //then
        assertEquals("Author does not exist", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void canNotEditAuthorMissingLibrarianId() {
        EditAuthorRequest request = new EditAuthorRequest();
        request.setFirstName(faker.name().firstName());
        request.setLastName(faker.name().lastName());

        when(authorRepo.findById(author.getId())).thenReturn(Optional.of(author));
        when(librarianRepo.findById(librarian.getId())).thenReturn(Optional.empty());

        ResponseEntity<AppResponse<String>> response = service.editAuthor(librarian.getId(), author.getId(), request);

        //then
        assertEquals("Unauthorized", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }

    @Test
    void canDeleteAuthor() {
        when(authorRepo.findById(author.getId())).thenReturn(Optional.of(author));
        when(librarianRepo.findById(librarian.getId())).thenReturn(Optional.of(librarian));


        ResponseEntity<AppResponse<String>> response = service.deleteAuthor(librarian.getId(), author.getId());

        //then
        assertEquals("Success", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());


    }

    @Test
    void canNotDeleteAuthorByLibrarianId() {
        when(authorRepo.findById(author.getId())).thenReturn(Optional.of(author));
        when(librarianRepo.findById(librarian.getId())).thenReturn(Optional.empty());


        ResponseEntity<AppResponse<String>> response = service.deleteAuthor(librarian.getId(), author.getId());

        //then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());


    }

}
