package com.etz.libraryapi.services;

import com.etz.libraryapi.config.Mapper;
import com.etz.libraryapi.domains.requests.CatalogRequest;
import com.etz.libraryapi.domains.requests.CreateNewCatalogRequest;
import com.etz.libraryapi.domains.requests.GenericDeleteRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.CatalogResponse;
import com.etz.libraryapi.models.Catalog;
import com.etz.libraryapi.models.Librarian;
import com.etz.libraryapi.repositories.CatalogueRepo;
import com.etz.libraryapi.repositories.LibrarianRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogService {
    private final CatalogueRepo catalogRepo;
    private final LibrarianRepo librarianRepo;
    private final Mapper mapper;

    public ResponseEntity<AppResponse<List<CatalogResponse>>> getCatalogs() {
        List<Catalog> isCatalog = catalogRepo.findAll();
        ArrayList<CatalogResponse> catalogResponses = new ArrayList<>();
        if (isCatalog.size() != 0) {
            for (Catalog catalog : isCatalog) {
                CatalogResponse response = mapper.modelMapper().map(catalog, CatalogResponse.class);
                response.setBooks(catalog.getCatalogBooks());
                catalogResponses.add(response);
            }
            return new ResponseEntity<>(new AppResponse<>(true, catalogResponses), HttpStatus.FOUND);
        }
        throw new IllegalStateException("Catalog list is empty");
    }

    public ResponseEntity<AppResponse<String>> createNewCatalog(CreateNewCatalogRequest request) {
        Optional<Librarian> librarian = librarianRepo.findById(request.getLibrarianId());
        if (librarian.isPresent()) {
            try {
                for (String category : request.getCatalogNames()) {
                    Catalog catalog = new Catalog();
                    catalog.setName(category.toUpperCase());
                    catalogRepo.save(catalog);
                }
                return new ResponseEntity<>(new AppResponse<>(true, "Successful"), HttpStatus.CREATED);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new IllegalStateException("Unauthorized");

    }

    public ResponseEntity<AppResponse<String>> editCatalog(Long id, CatalogRequest request) {
        Optional<Librarian> librarian = librarianRepo.findById(request.getLibrarianId());
        if (librarian.isPresent()) {
            Catalog catalog = catalogRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Catalog does not exist"));
            catalog.setName(request.getName().toUpperCase());
            catalogRepo.save(catalog);
            return new ResponseEntity<>(new AppResponse<>(true, "Successful"), HttpStatus.OK);
        }
        throw new IllegalStateException("Unauthorized");

    }

    public ResponseEntity<AppResponse<?>> deleteCatalog(Long id, GenericDeleteRequest request) {
        Optional<Librarian> librarian = librarianRepo.findById(request.getLibrarianId());
        if (librarian.isPresent()) {
            Catalog catalog = catalogRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Catalog does not exist"));
            log.info("Optional isCatalog: {}", catalog);
            catalogRepo.delete(catalog);
            return new ResponseEntity<>(new AppResponse<>(true, ""), HttpStatus.NO_CONTENT);
        }
        throw new IllegalStateException("Unauthorized");
    }
}
