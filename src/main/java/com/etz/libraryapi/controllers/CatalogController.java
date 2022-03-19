package com.etz.libraryapi.controllers;

import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.CatalogResponse;
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

    @GetMapping
    public ResponseEntity<AppResponse<List<CatalogResponse>>> getCatalog() {
        return catalogService.getCatalogs();
    }

}
