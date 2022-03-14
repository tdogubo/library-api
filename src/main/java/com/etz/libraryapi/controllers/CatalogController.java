package com.etz.libraryapi.controllers;

import com.etz.libraryapi.domains.requests.CatalogRequest;
import com.etz.libraryapi.domains.requests.CreateNewCatalogRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.models.Catalog;
import com.etz.libraryapi.services.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalogs")
public class CatalogController {
    private final CatalogService catalogService;

    @PostMapping
    public ResponseEntity<AppResponse<String>> addCatalog(@RequestBody CreateNewCatalogRequest catalogs) {
        return catalogService.createNewCatalog(catalogs);
    }

    @PatchMapping("{id}")
    public ResponseEntity<AppResponse<String>> editCatalog(@PathVariable("id") Long id, @RequestBody CatalogRequest newCatalogName) {
        return catalogService.editCatalog(id, newCatalogName);
    }

    @GetMapping
    public ResponseEntity<AppResponse<List<Catalog>>> getCatalog() {
        return catalogService.getCatalogs();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<AppResponse<?>> deleteCatalog(@PathVariable("id") Long id) {
        return catalogService.deleteCatalog(id);
    }
}
