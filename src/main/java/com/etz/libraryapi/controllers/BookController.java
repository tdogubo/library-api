package com.etz.libraryapi.controllers;

import com.etz.libraryapi.domains.requests.BookRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.BookResponse;
import com.etz.libraryapi.models.Book;
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

    @GetMapping
    public ResponseEntity<AppResponse<List<BookResponse>>> getBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping //string parameters
    public ResponseEntity<AppResponse<BookResponse>> searchBook(@Valid @RequestBody BookRequest request) {
        return bookService.searchBook(request);
    }

    @PostMapping("/{id}/borrow/{memberId}")
    public ResponseEntity<AppResponse<Book>> borrowBook(@PathVariable("id") Long id, @PathVariable("memberId") UUID memberId) {
        return bookService.borrowBook(id, memberId);
    }

}
