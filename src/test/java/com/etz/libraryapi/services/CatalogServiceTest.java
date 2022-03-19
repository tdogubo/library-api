package com.etz.libraryapi.services;

import com.etz.libraryapi.domains.requests.CatalogRequest;
import com.etz.libraryapi.domains.requests.CreateNewCatalogRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.CatalogResponse;
import com.etz.libraryapi.models.Book;
import com.etz.libraryapi.models.Catalog;
import com.etz.libraryapi.models.Librarian;
import com.etz.libraryapi.repositories.CatalogueRepo;
import com.etz.libraryapi.repositories.LibrarianRepo;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
public class CatalogServiceTest {
    Faker faker = new Faker();
    Librarian testLibrarian = new Librarian();
    @Autowired
    private CatalogService testService;
    @MockBean
    private CatalogueRepo catalogRepo;
    @MockBean
    private LibrarianRepo librarianRepo;

    @BeforeEach
    void setUp() {
        testLibrarian.setId(UUID.randomUUID());
        testLibrarian.setEmail(faker.internet().emailAddress());
    }

    @Test
    void canGetCatalogs() {
        Book book = new Book();
        book.setTitle(faker.book().title());
        book.setId(1L);

        ArrayList<Book> books = new ArrayList<>();
        books.add(book);

        Catalog catalog = new Catalog();
        catalog.setName("Catalog");
        catalog.setId(1L);
        catalog.setBooks(books);

        List<Catalog> testList = new ArrayList<>();
        testList.add(catalog);

        when(catalogRepo.findAll()).thenReturn(testList);

        ResponseEntity<AppResponse<List<CatalogResponse>>> response = testService.getCatalogs();

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(testList.get(0).getName(), Objects.requireNonNull(response.getBody()).getData().get(0).getName());
    }

    @Test
    void canGetEmptyCatalogs() {

        when(catalogRepo.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<AppResponse<List<CatalogResponse>>> response = testService.getCatalogs();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Catalog list is empty", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void canCreateCatalog() {
        ArrayList<String> catalogNames = new ArrayList<>();
        catalogNames.add("History");
        catalogNames.add("fiction");

        CreateNewCatalogRequest request = new CreateNewCatalogRequest();
        request.setCatalogNames(catalogNames);

        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.of(testLibrarian));

        ResponseEntity<AppResponse<String>> response = testService.createNewCatalog(testLibrarian.getId(), request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Successful", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void canNotCreateCatalog() {
        ArrayList<String> catalogNames = new ArrayList<>();
        catalogNames.add("History");
        catalogNames.add("fiction");

        CreateNewCatalogRequest request = new CreateNewCatalogRequest();
        request.setCatalogNames(catalogNames);

        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.empty());

        ResponseEntity<AppResponse<String>> response = testService.createNewCatalog(testLibrarian.getId(), request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Unauthorized", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void canEditCatalog() {
        CatalogRequest request = new CatalogRequest();
        request.setName("History");

        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.of(testLibrarian));
        when(catalogRepo.findById(anyLong())).thenReturn(Optional.of(new Catalog()));

        ResponseEntity<AppResponse<String>> response = testService.editCatalog(testLibrarian.getId(), anyLong(), request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals("Successful", response.getBody().getMessage());
    }

    @Test
    void canNotEditCatalog() {
        CatalogRequest request = new CatalogRequest();
        request.setName("History");

        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.empty());
        when(catalogRepo.findById(anyLong())).thenReturn(Optional.of(new Catalog()));

        ResponseEntity<AppResponse<String>> response = testService.editCatalog(testLibrarian.getId(), anyLong(), request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(false, Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals("Unauthorized", response.getBody().getMessage());
    }

    @Test
    void canDeleteCatalog() {
        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.of(testLibrarian));
        when(catalogRepo.findById(anyLong())).thenReturn(Optional.of(new Catalog()));
        ResponseEntity<AppResponse<String>> response = testService.deleteCatalog(testLibrarian.getId(), anyLong());

        assertEquals("", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(true, response.getBody().getStatus());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }

    @Test
    void canNotDeleteCatalog() {
        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.empty());
        when(catalogRepo.findById(anyLong())).thenReturn(Optional.of(new Catalog()));
        ResponseEntity<AppResponse<String>> response = testService.deleteCatalog(testLibrarian.getId(), anyLong());

        assertEquals("Unauthorized", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(false, response.getBody().getStatus());

    }
}
