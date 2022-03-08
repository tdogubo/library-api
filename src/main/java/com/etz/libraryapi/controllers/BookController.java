package com.etz.libraryapi.controllers;

import com.etz.libraryapi.domains.requests.AddBookRequest;
import com.etz.libraryapi.domains.responses.AddBookResponse;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<AppResponse<AddBookResponse>> newBook( @Valid @RequestBody AddBookRequest request){
        return bookService.newBook(request);
    }
}
