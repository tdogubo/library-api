package com.etz.libraryapi.services;

import com.etz.libraryapi.config.Mapper;
import com.etz.libraryapi.domains.requests.AddBookRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.BookResponse;
import com.etz.libraryapi.models.Author;
import com.etz.libraryapi.models.Book;
import com.etz.libraryapi.models.Catalog;
import com.etz.libraryapi.repositories.AuthorRepo;
import com.etz.libraryapi.repositories.BookRepo;
import com.etz.libraryapi.repositories.CatalogueRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final AuthorRepo authorRepo;
    private final BookRepo bookRepo;
    private final CatalogueRepo catalogueRepo;
    private final Mapper mapper;

    private String generateCallNumber(String title, String catalog, String publishYear) {
        String firstValue = catalog.toUpperCase();
        String secondValue = title.toUpperCase();
        return firstValue + secondValue.substring(0, 3) + publishYear;

    }

    public ResponseEntity<AppResponse<BookResponse>> newBook(AddBookRequest request) {
        Catalog catalog = catalogueRepo.findByName(request.getCatalog()).orElseThrow(() -> new IllegalStateException("Catalog does not exist!!"));

        Book newBook = new Book();
        newBook.setIsbn(request.getIsbn());
        newBook.setTitle(request.getTitle());
        newBook.setPublisher(request.getPublisher());
        newBook.setGenre(request.getGenre());
        newBook.setPublishYear(request.getPublishYear()); // extract year only from date
        newBook.setDescription(request.getDescription());
        newBook.setLanguage(request.getLanguage());
        newBook.setPages(request.getPages());
        newBook.setCatalog(catalog);
        newBook.setCallNumber(generateCallNumber(request.getTitle(), request.getCatalog(), request.getPublishYear()));
        for (String author : request.getAuthor()) {
            String authorFirstName = author.split(" ")[0];
            String authorLastName = author.split(" ")[1];

            Optional<Author> isAuthorInDatabase = authorRepo.findByFirstNameAndLastName(authorFirstName, authorLastName);
            if (isAuthorInDatabase.isPresent()) {
                Author savedAuthor = isAuthorInDatabase.get();
                log.info("AUTHOR IN DATABASE :: {}", savedAuthor);
                newBook.addAuthor(savedAuthor);
                bookRepo.save(newBook);
            } else {
                Author saveAuthor = new Author();
                saveAuthor.setFirstName(authorFirstName);
                saveAuthor.setLastName(authorLastName);
                authorRepo.save(saveAuthor);
                newBook.addAuthor(saveAuthor);
                bookRepo.save(newBook);
                log.info("BOOK ID:: " + newBook.getId());
            }

        }
        BookResponse response = mapper.modelMapper().map(newBook, BookResponse.class);
        response.setAuthors(request.getAuthor());
        return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.CREATED);


    }

//    public ResponseEntity<AppResponse<BookResponse>> editBookDetails(){
//
//    }
}
