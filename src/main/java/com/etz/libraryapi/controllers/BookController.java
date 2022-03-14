package com.etz.libraryapi.controllers;

import com.etz.libraryapi.domains.requests.AddBookRequest;
import com.etz.libraryapi.domains.requests.EditBookRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.BookResponse;
import com.etz.libraryapi.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<AppResponse<List<BookResponse>>> getbooks() {
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
    public ResponseEntity<AppResponse<String>> deleteBook(@PathVariable("id") Long id) {
        return bookService.deleteBook(id);
    }
}
