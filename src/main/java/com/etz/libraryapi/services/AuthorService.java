package com.etz.libraryapi.services;

import com.etz.libraryapi.domains.requests.EditAuthorRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.models.Author;
import com.etz.libraryapi.repositories.AuthorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class AuthorService {
    private final AuthorRepo authorRepo;

    public ResponseEntity<AppResponse<String>> editAuthor(UUID id, EditAuthorRequest request) {
        Optional<Author> isAuthor = authorRepo.findById(id);
        if (isAuthor.isPresent()) {
            Author author = isAuthor.get();
            author.setFirstName(request.getFirstName());
            author.setLastName(request.getLastName());
            authorRepo.save(author);
            return new ResponseEntity<>(new AppResponse<>(true, ""), HttpStatus.ACCEPTED);
        }
        throw new IllegalStateException("Author does not exist!!");

    }

    public ResponseEntity<AppResponse<String>> deleteAuthor(UUID id) {
        Optional<Author> isAuthor = authorRepo.findById(id);
        if (isAuthor.isPresent()) {
            Author author = isAuthor.get();
            authorRepo.delete(author);
            return new ResponseEntity<>(new AppResponse<>(true, "Success"), HttpStatus.NO_CONTENT);
        }
        throw new IllegalArgumentException("Author not found!!");
    }
}
