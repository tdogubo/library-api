package com.etz.libraryapi.services;

import com.etz.libraryapi.domains.requests.CatalogRequest;
import com.etz.libraryapi.domains.requests.CreateNewCatalogRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.models.Catalog;
import com.etz.libraryapi.repositories.AuthorRepo;
import com.etz.libraryapi.repositories.CatalogueRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private final CatalogueRepo catalogRepo;
    private final AuthorRepo authorRepo;

    public ResponseEntity<AppResponse<String>> createNewCatalog(CreateNewCatalogRequest request) {
        try {
            for (String category : request.getCatalogNames()) {
                Catalog catalog = new Catalog();
                catalog.setName(category);
                catalogRepo.save(catalog);
            }
            return new ResponseEntity<>(new AppResponse<>(true, "Successful"), HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ResponseEntity<AppResponse<String>> editCatalog(CatalogRequest request) {
        Optional<Catalog> isCatalog = catalogRepo.findByName(request.getName());
        if (isCatalog.isPresent()) {
            Catalog catalog = isCatalog.get();
            catalog.setName(request.getName());
            catalogRepo.save(catalog);
            return new ResponseEntity<>(new AppResponse<>(true, "Successful"), HttpStatus.FOUND);
        }
        throw new IllegalArgumentException("Catalog name does not exist");
    }

    public ResponseEntity<AppResponse<String>> removeCatalog(CatalogRequest request) {
        Optional<Catalog> isCatalog = catalogRepo.findByName(request.getName());
        if (isCatalog.isPresent()) {
            Catalog catalog = isCatalog.get();
            catalogRepo.delete(catalog);
            return new ResponseEntity<>(new AppResponse<>(true, "Successful"), HttpStatus.FOUND);
        }
        throw new IllegalArgumentException("Catalog name does not exist");
    }

//    public ResponseEntity<AppResponse<BookResponse>> addBook (AddBookRequest request) {
//        try {
//            Book book = new Book();
//            book.setTitle(request.getTitle());
//            for (String author : request.getAuthor()) {
//                Optional<Author> isAuthor = authorRepo.findByName(author);
//
//
//            }
//            book.setAuthor(request.getAuthor());
//            book.setDescription(request.getDescription());
//            book.setCatalog(request.getCatalog());
//            book.setIsbn(request.getIsbn());
//            book.setImage(request.getImage());
//            book.setStatus(request.getStatus());
//
//            }
//            return new ResponseEntity<>(new AppResponse<>(true, "Successful"), HttpStatus.CREATED);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }


}
