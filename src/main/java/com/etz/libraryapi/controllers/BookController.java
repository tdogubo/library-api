package com.etz.libraryapi.controllers;

import com.etz.libraryapi.domains.requests.*;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.BookResponse;
import com.etz.libraryapi.domains.responses.BorrowBookResponse;
import com.etz.libraryapi.services.AuthorService;
import com.etz.libraryapi.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<AppResponse<List<BookResponse>>> getBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping
    public ResponseEntity<AppResponse<BookResponse>> newBook(@Valid @RequestBody AddBookRequest request) {
        return bookService.newBook(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppResponse<BookResponse>> editBook(@PathVariable("id") Long id, @Valid @RequestBody EditBookRequest request) {
        return bookService.editBookDetails(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse<String>> deleteBook(@PathVariable("id") Long id, @Valid @RequestBody GenericDeleteRequest request) {
        return bookService.deleteBook(id, request);
    }

    @PatchMapping("/authors/{id}")
    public ResponseEntity<AppResponse<String>> editAuthor(@PathVariable("id") UUID id, @Valid @RequestBody EditAuthorRequest request) {
        return authorService.editAuthor(id, request);
    }

    @DeleteMapping("/authors/{id}")
    public ResponseEntity<AppResponse<String>> deleteAuthor(@PathVariable("id") UUID id) {
        return authorService.deleteAuthor(id);
    }

    @PostMapping("/borrow/{id}")
    public ResponseEntity<AppResponse<BorrowBookResponse>> borrowBook(@PathVariable("id") UUID id, @Valid @RequestBody BorrowBookRequest request) {
        return bookService.borrowBook(id, request);
    }

}
