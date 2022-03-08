package com.etz.libraryapi.services;

import com.etz.libraryapi.config.Mapper;
import com.etz.libraryapi.domains.requests.AddBookRequest;
import com.etz.libraryapi.domains.responses.AddBookResponse;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.models.Author;
import com.etz.libraryapi.models.Book;
import com.etz.libraryapi.repositories.AuthorRepo;
import com.etz.libraryapi.repositories.BookRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final AuthorRepo authorRepo;
    private final BookRepo bookRepo;
    private final Mapper mapper;

    public ResponseEntity<AppResponse<AddBookResponse>> newBook(AddBookRequest request) {
        Book newBook = new Book();
        newBook.setIsbn(request.getIsbn());
        newBook.setTitle(request.getTitle());
        newBook.setPublisher(request.getPublisher());
        newBook.setGenre(request.getGenre());
        newBook.setPublicationDate(request.getPublicationDate());
        newBook.setDescription(request.getDescription());
        newBook.setLanguage(request.getLanguage());
        newBook.setPages(request.getPages());

        for (String author : request.getAuthor()) {
            String authorFirstName = author.split(" ")[0];
            String authorLastName = author.split(" ")[1];

            Author saveAuthor = new Author();
            saveAuthor.setFirstName(authorFirstName);
            saveAuthor.setLastName(authorLastName);
            authorRepo.save(saveAuthor);

            newBook.addAuthor(saveAuthor);
            bookRepo.save(newBook);
        }
        log.info("BOOK CREATED ::: {}", newBook);
        AddBookResponse response = mapper.modelMapper().map(newBook, AddBookResponse.class);
        response.setAuthors(request.getAuthor());
        return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.CREATED);

        }
    }
