package com.etz.libraryapi.controllers;

import com.etz.libraryapi.domains.requests.CatalogRequest;
import com.etz.libraryapi.domains.requests.CreateNewCatalogRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.services.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/catalog")
    public ResponseEntity<AppResponse<String>> addCatalog(@RequestBody CreateNewCatalogRequest request) {
        return bookService.createNewCatalog(request);
    }

    @PatchMapping("/catalog")
    public ResponseEntity<AppResponse<String>> editCatalog(@RequestBody CatalogRequest request) {
        return bookService.editCatalog(request);
    }
}
