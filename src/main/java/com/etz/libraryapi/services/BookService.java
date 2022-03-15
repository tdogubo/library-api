package com.etz.libraryapi.services;

import com.etz.libraryapi.config.Mapper;
import com.etz.libraryapi.domains.requests.BorrowBookRequest;
import com.etz.libraryapi.domains.requests.GenericDeleteRequest;
import com.etz.libraryapi.domains.requests.AddBookRequest;
import com.etz.libraryapi.domains.requests.EditBookRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.BookResponse;
import com.etz.libraryapi.domains.responses.BorrowBookResponse;
import com.etz.libraryapi.models.*;
import com.etz.libraryapi.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepo bookRepo;
    private final AuthorRepo authorRepo;
    private final MemberRepo memberRepo;
    private final CatalogueRepo catalogueRepo;
    private final LibrarianRepo librarianRepo;
    private final BorrowHistoryRepo borrowHistoryRepo;
    private final Mapper mapper;

    private String generateCallNumber(String title, String catalog, String publishYear) {
        String firstValue = catalog.toUpperCase();
        String secondValue = title.toUpperCase();
        return firstValue + secondValue.substring(0, 3) + publishYear;

    }

    public ResponseEntity<AppResponse<List<BookResponse>>> getAllBooks() {
        List<Book> books = bookRepo.findAll();
        ArrayList<BookResponse> response = new ArrayList<>();
        for (Book book : books) {
            BookResponse bookResponse = mapper.modelMapper().map(book, BookResponse.class);
            bookResponse.setAuthors(book.getBookAuthors());
            bookResponse.setCatalog(book.getCatalog().getName());
            response.add(bookResponse);
        }
        return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.OK);
    }

    public ResponseEntity<AppResponse<BookResponse>> newBook(AddBookRequest request) {
        Optional<Librarian> librarian = librarianRepo.findById(request.getLibrarianId());
        if (librarian.isPresent()) {
            Catalog catalog = catalogueRepo.findByName(request.getCatalog().toUpperCase()).orElseThrow(() -> new IllegalStateException("Catalog does not exist!!"));

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
                } else {
                    Author saveAuthor = new Author();
                    saveAuthor.setFirstName(authorFirstName);
                    saveAuthor.setLastName(authorLastName);
                    authorRepo.save(saveAuthor);
                    newBook.addAuthor(saveAuthor);
                }

            }
            bookRepo.save(newBook);
            BookResponse response = mapper.modelMapper().map(newBook, BookResponse.class);
            response.setCatalog(newBook.getCatalog().getName());
            response.setAuthors(newBook.getBookAuthors());
            return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.CREATED);
        }
        throw new IllegalStateException("Unauthorized");


    }

    public ResponseEntity<AppResponse<BookResponse>> editBookDetails(Long id, EditBookRequest request) {
        Optional<Librarian> librarian = librarianRepo.findById(request.getLibrarianId());
        if (librarian.isPresent()) {
            Book book = bookRepo.findById(id).orElseThrow(() -> new IllegalStateException("Book does not exist"));
            book.setTitle(request.getTitle());
            book.setDescription(request.getDescription());
            book.setCopies(book.getCopies() + request.getCopies());

            if (!request.getDeleteAuthor().isEmpty()) {
                for (String author : request.getDeleteAuthor()) {
                    String firstName = author.split(" ")[0];
                    String lastName = author.split(" ")[1];
                    Author foundAuthor = authorRepo.findByFirstNameAndLastName(firstName, lastName).orElseThrow(() -> new IllegalStateException("Author does not exist"));

                    book.getAuthors().remove(foundAuthor);
                }
            }
            if (!request.getAddAuthor().isEmpty()) {
                for (String author : request.getAddAuthor()) {
                    String authorFirstName = author.split(" ")[0];
                    String authorLastName = author.split(" ")[1];

                    Optional<Author> isAuthorInDatabase = authorRepo.findByFirstNameAndLastName(authorFirstName, authorLastName);
                    if (isAuthorInDatabase.isPresent()) {
                        Author savedAuthor = isAuthorInDatabase.get();
                        log.info("AUTHOR IN DATABASE :: {}", savedAuthor);
                        book.addAuthor(savedAuthor);
                    } else {
                        Author saveAuthor = new Author();
                        saveAuthor.setFirstName(authorFirstName);
                        saveAuthor.setLastName(authorLastName);
                        authorRepo.save(saveAuthor);
                        book.addAuthor(saveAuthor);
                    }

                }

            }
            bookRepo.save(book);
            BookResponse response = mapper.modelMapper().map(book, BookResponse.class);
            response.setCatalog(book.getCatalog().getName());
            response.setAuthors(book.getBookAuthors());
            return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.ACCEPTED);
        }
        throw new IllegalStateException("Unauthorized");
    }

    public ResponseEntity<AppResponse<String>> deleteBook(Long id, GenericDeleteRequest request) {
        Optional<Librarian> librarian = librarianRepo.findById(request.getLibrarianId());
        if (librarian.isPresent()) {
            Book book = bookRepo.findById(id).orElseThrow(() -> new IllegalStateException("Book does not exist"));
            bookRepo.delete(book);
            return new ResponseEntity<>(new AppResponse<>(true, ""), HttpStatus.NO_CONTENT);
        }
        throw new IllegalStateException("Unauthorized");
    }

    public ResponseEntity<AppResponse<BorrowBookResponse>> borrowBook(UUID id, BorrowBookRequest request) {
        Member member = memberRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("SignUp to borrow books."));
        if(member.getIsActive()){
            BorrowHistory newBorrow = new BorrowHistory();
            newBorrow.setDueDate(newBorrow.getCreationDate().plusDays(7));
            if(member.getLibraryCard().getTier()>1){
                // check the tier of the member if it is less than 1 and limit the number of books the person can borrow
            }
        } throw new IllegalStateException("Activate your account");

    }
}
