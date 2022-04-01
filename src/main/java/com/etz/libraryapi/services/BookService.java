package com.etz.libraryapi.services;

import com.etz.libraryapi.config.Mapper;
import com.etz.libraryapi.domains.requests.AddBookRequest;
import com.etz.libraryapi.domains.requests.BookRequest;
import com.etz.libraryapi.domains.requests.EditBookRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.BookResponse;
import com.etz.libraryapi.models.*;
import com.etz.libraryapi.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final LibraryCardRepo libraryCardRepo;
    private final LibrarianRepo librarianRepo;
    private final BorrowHistoryRepo borrowHistoryRepo;
    private final Mapper mapper;

    protected String generateCallNumber(String title, String catalog, String publishYear) {
        String firstValue = catalog.toUpperCase();
        String secondValue = title.toUpperCase();
        return firstValue + secondValue.substring(0, 3) + publishYear;

    }

    public ResponseEntity<AppResponse<List<BookResponse>>> getAllBooks() {
        List<Book> books = bookRepo.findAll();
        if (books.size() != 0) {
            ArrayList<BookResponse> response = new ArrayList<>();
            for (Book book : books) {
                BookResponse bookResponse = mapper.modelMapper().map(book, BookResponse.class);
                bookResponse.setAuthorList(book.getBookAuthors());
                bookResponse.setCatalog(book.getCatalog().getName());
                response.add(bookResponse);
            }
            return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.OK);
        }
        return new ResponseEntity<>(new AppResponse<>(true, "No books yet"), HttpStatus.OK);
    }

    public ResponseEntity<AppResponse<BookResponse>> newBook(UUID librarianId, AddBookRequest request) {
        Optional<Librarian> librarian = librarianRepo.findById(librarianId);
        if (librarian.isPresent()) {
            Optional<Catalog> catalog = catalogueRepo.findByName(request.getCatalog().toUpperCase());
            if (catalog.isPresent()) {
                Book newBook = new Book();
                newBook.setIsbn(request.getIsbn());
                newBook.setTitle(request.getTitle());
                newBook.setPublisher(request.getPublisher());
                newBook.setGenre(request.getGenre());
                newBook.setPublishYear(request.getPublishYear());
                newBook.setDescription(request.getDescription());
                newBook.setLanguage(request.getLanguage());
                newBook.setPages(request.getPages());
                newBook.setCatalog(catalog.get());
                newBook.setCopies(request.getCopies());
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
                response.setAuthorList(newBook.getBookAuthors());
                return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new AppResponse<>(false, "Catalog does not exist!!"), HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(new AppResponse<>(false, "Unauthorized"), HttpStatus.FORBIDDEN);

    }

    public ResponseEntity<AppResponse<BookResponse>> editBookDetails(UUID librarianId, Long bookId, EditBookRequest request) {
        Optional<Librarian> librarian = librarianRepo.findById(librarianId);
        if (librarian.isPresent()) {
            Book book = bookRepo.findById(bookId).orElseThrow(() -> new IllegalStateException("Book does not exist"));
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
            response.setAuthorList(book.getBookAuthors());
            return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(new AppResponse<>(false, "Unauthorized"), HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<AppResponse<BookResponse>> searchBook(BookRequest request) {
        Optional<Book> isBook = bookRepo.findByTitleAndAuthorFirstNameAndLastName(request.getTitle(), request.getFirstName(), request.getLastName());
        log.info("{}", isBook);
        if (isBook.isPresent()) {
            Book book = isBook.get();
            log.info("{}", book.getAuthors());
            BookResponse response = mapper.modelMapper().map(book, BookResponse.class);
            response.setCatalog(book.getCatalog().getName());
            response.setAuthorList(book.getBookAuthors());
            return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(new AppResponse<>(true, "Not found"), HttpStatus.NOT_FOUND);

    }


    public ResponseEntity<AppResponse<String>> deleteBook(UUID librarianId, Long bookId) {
        Optional<Librarian> librarian = librarianRepo.findById(librarianId);
        if (librarian.isPresent()) {
            Book book = bookRepo.findById(bookId).orElseThrow(() -> new IllegalStateException("Book does not exist"));
            bookRepo.delete(book);
            return new ResponseEntity<>(new AppResponse<>(true, ""), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new AppResponse<>(false, "Unauthorized"), HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<AppResponse<String>> borrowBook(Long id, UUID memberId) {
        Member member = memberRepo.findById(memberId).orElseThrow(() -> new IllegalArgumentException("SignUp to borrow books."));
        if (member.getIsActive()) {
            LibraryCard card = libraryCardRepo.findByMemberId(memberId).orElseThrow(()-> new IllegalArgumentException("Member not associated with card"));
            if (card.getFine() != 0) {
                return new ResponseEntity<>(new AppResponse<>(false, "Please clear your fine"), HttpStatus.OK);
            } else {
                if (card.getMaxNumberOfBooks() >= card.getNumberOfBooksCheckedOut()) {
                    Book book = bookRepo.findById(id).orElseThrow(() -> new IllegalStateException("Not found"));
                    if (book.getCopies() != 0) {
                        BorrowHistory newBorrow = new BorrowHistory();
                        newBorrow.setDueDate(newBorrow.getCreationDate().plusDays(7));
                        newBorrow.setLibraryCard(card);
                        newBorrow.setBook(book);
                        card.setNumberOfBooksCheckedOut(card.getNumberOfBooksCheckedOut() + 1);
                        book.setCopies(book.getCopies() - 1);

                        borrowHistoryRepo.save(newBorrow);

                        card.setHistory(newBorrow);
                        libraryCardRepo.save(card);
                        bookRepo.save(book);

                        return new ResponseEntity<>(new AppResponse<>(true, "Request successful"), HttpStatus.OK);

                    }
                    return new ResponseEntity<>(new AppResponse<>(false, "No copies available. Try again later"), HttpStatus.NOT_FOUND);

                }
                return new ResponseEntity<>(new AppResponse<>(false, "Max number of books allowed exceeded. Return some and try again."), HttpStatus.OK);

            }
        }
        return new ResponseEntity<>(new AppResponse<>(false, "Activate your account"), HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<AppResponse<Book>> returnBook(UUID borrowId) {
        BorrowHistory history = borrowHistoryRepo.findById(borrowId).orElseThrow(() -> new IllegalArgumentException("SignUp to borrow books."));
        history.setReturnDate(LocalDate.now());
        Book book = history.getBook();
        book.setCopies(book.getCopies() + 1);
        bookRepo.save(book);

        LibraryCard card = history.getLibraryCard();
        card.setNumberOfBooksCheckedOut(card.getNumberOfBooksCheckedOut() - 1);
        if (!history.getDueDate().isAfter(history.getReturnDate()) || !history.getDueDate().isEqual(history.getReturnDate()))
            card.setFine(card.getFine() + 100.00);

        libraryCardRepo.save(card);
        return new ResponseEntity<>(new AppResponse<>(true, "Returned"), HttpStatus.OK);
    }
}
