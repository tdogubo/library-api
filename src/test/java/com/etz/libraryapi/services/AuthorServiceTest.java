package com.etz.libraryapi.services;

import com.etz.libraryapi.domains.requests.EditAuthorRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.models.Author;
import com.etz.libraryapi.repositories.AuthorRepo;
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
    Faker faker = new Faker();
    @Autowired
    private AuthorService service;
    @MockBean
    private AuthorRepo authorRepo;

    @BeforeEach
    void setUp() {
        author.setFirstName(faker.name().firstName());
        author.setLastName(faker.name().lastName());
        author.setId(UUID.randomUUID());
    }

    @Test
    void canEditAuthor() {
        EditAuthorRequest request = new EditAuthorRequest();
        request.setFirstName(faker.name().firstName());
        request.setLastName(faker.name().lastName());

        when(authorRepo.findById(author.getId())).thenReturn(Optional.of(author));

        ResponseEntity<AppResponse<String>> response = service.editAuthor(author.getId(), request);

        //then
        assertEquals("", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

    }

    @Test
    void canDeleteAuthor() {
        when(authorRepo.findById(author.getId())).thenReturn(Optional.of(author));

        ResponseEntity<AppResponse<String>> response = service.deleteAuthor(author.getId());

        //then
        assertEquals("Success", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());


    }

}
