package com.etz.libraryapi.controllers;

import com.etz.libraryapi.domains.requests.*;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.BookResponse;
import com.etz.libraryapi.domains.responses.UserResponse;
import com.etz.libraryapi.services.AuthorService;
import com.etz.libraryapi.services.BookService;
import com.etz.libraryapi.services.CatalogService;
import com.etz.libraryapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class LibrarianController {
    private final AuthorService authorService;
    private final UserService userService;
    private final CatalogService catalogService;
    private final BookService bookService;

    @PatchMapping("/users/{id}")
    public ResponseEntity<AppResponse<UserResponse>> changeUserStatus(@PathVariable("id") UUID id, @Valid @RequestBody ChangeUserStatusRequest request) {
        return userService.changeUserStatus(id, request);
    }

    @PatchMapping("/{librarianId}/authors/{authorId}")
    public ResponseEntity<AppResponse<String>> editAuthor(@PathVariable("librarianId") UUID librarianId, @PathVariable("authorId") UUID authorId, @Valid @RequestBody EditAuthorRequest request) {
        return authorService.editAuthor(librarianId, authorId, request);
    }

    @DeleteMapping("/{librarianId}/authors/{authorId}")
    public ResponseEntity<AppResponse<String>> deleteAuthor(@PathVariable("librarianId") UUID librarianId, @PathVariable("authorId") UUID authorId) {
        return authorService.deleteAuthor(librarianId, authorId);
    }

    @PostMapping("/{librarianId}/catalogs")
    public ResponseEntity<AppResponse<String>> addCatalog(@PathVariable("librarianId") UUID librarianId, @RequestBody @Valid CreateNewCatalogRequest catalogs) {
        return catalogService.createNewCatalog(librarianId, catalogs);
    }

    @PatchMapping("/{librarianId}/catalogs/{catalogId}")
    public ResponseEntity<AppResponse<String>> editCatalog(@PathVariable("librarianId") UUID librarianId,
                                                           @PathVariable("catalogId") Long catalogId,
                                                           @Valid @RequestBody CatalogRequest newCatalogName) {
        return catalogService.editCatalog(librarianId, catalogId, newCatalogName);
    }

    @DeleteMapping("/{librarianId}/catalogs/{catalogId}")
    public ResponseEntity<AppResponse<String>> deleteCatalog(@PathVariable("librarianId") UUID librarianId, @PathVariable("catalogId") Long catalogId) {
        return catalogService.deleteCatalog(librarianId, catalogId);
    }

    @PostMapping("/{librarianId}/books")
    public ResponseEntity<AppResponse<BookResponse>> newBook(@PathVariable("librarianId") UUID librarianId, @Valid @RequestBody AddBookRequest request) {
        return bookService.newBook(librarianId, request);
    }

    @PutMapping("/{librarianId}/books/{bookId}")
    public ResponseEntity<AppResponse<BookResponse>> editBook(@PathVariable("librarianId") UUID librarianId, @PathVariable("bookId") Long bookId, @Valid @RequestBody EditBookRequest request) {
        return bookService.editBookDetails(librarianId, bookId, request);
    }

    @DeleteMapping("/{librarianId}/books/{bookId}")
    public ResponseEntity<AppResponse<String>> deleteBook(@PathVariable("librarianId") UUID librarianId, @PathVariable("bookId") Long bookId) {
        return bookService.deleteBook(librarianId, bookId);
    }
}
