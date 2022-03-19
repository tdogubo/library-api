package com.etz.libraryapi.services;

import com.etz.libraryapi.domains.requests.EditAuthorRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.models.Author;
import com.etz.libraryapi.models.Librarian;
import com.etz.libraryapi.repositories.AuthorRepo;
import com.etz.libraryapi.repositories.LibrarianRepo;
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
    private final LibrarianRepo librarianRepo;

    public ResponseEntity<AppResponse<String>> editAuthor(UUID librarianId, UUID authorId, EditAuthorRequest request) {
        Optional<Librarian> librarian = librarianRepo.findById(librarianId);
        if (librarian.isPresent()) {
            Optional<Author> isAuthor = authorRepo.findById(authorId);
            if (isAuthor.isPresent()) {
                Author author = isAuthor.get();
                author.setFirstName(request.getFirstName());
                author.setLastName(request.getLastName());
                authorRepo.save(author);
                return new ResponseEntity<>(new AppResponse<>(true, ""), HttpStatus.ACCEPTED);
            }
            return new ResponseEntity<>(new AppResponse<>(false, "Author does not exist"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new AppResponse<>(false, "Unauthorized"), HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<AppResponse<String>> deleteAuthor(UUID librarianId, UUID authorId) {
        Optional<Librarian> librarian = librarianRepo.findById(librarianId);
        if (librarian.isPresent()) {
            Author author = authorRepo.findById(authorId).orElseThrow(() -> new IllegalArgumentException("Author not found!!"));
            authorRepo.delete(author);
            return new ResponseEntity<>(new AppResponse<>(true, "Success"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new AppResponse<>(false, "Unauthorized"), HttpStatus.FORBIDDEN);
    }
}
