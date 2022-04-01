package com.etz.libraryapi.services;

import com.etz.libraryapi.domains.requests.AddBookRequest;
import com.etz.libraryapi.domains.requests.EditBookRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.BookResponse;
import com.etz.libraryapi.models.*;
import com.etz.libraryapi.repositories.*;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
public class BookServiceTest {
    Faker faker = new Faker();
    Book testBook = new Book();
    Author author = new Author();
    Catalog catalog = new Catalog();
    List<String> authors = new ArrayList<>();
    Member testMember = new Member();
    LibraryCard testCard = new LibraryCard();
    Librarian testLibrarian = new Librarian();
    AddBookRequest request = new AddBookRequest();
    BookResponse expectedResponse = new BookResponse();

    @Autowired
    private BookService testService;

    @MockBean
    private BookRepo bookRepo;

    @MockBean
    private AuthorRepo authorRepo;

    @MockBean
    private MemberRepo memberRepo;

    @MockBean
    private CatalogueRepo catalogueRepo;

    @MockBean
    private LibrarianRepo librarianRepo;

    @MockBean
    private LibraryCardRepo libraryCardRepo;

    @MockBean
    private BorrowHistoryRepo borrowHistoryRepo;

    @BeforeEach
    void setUp() {

        catalog.setId(new Random().nextLong());
        catalog.setName(faker.book().toString());

        author.setId(UUID.randomUUID());
        author.setLastName(faker.name().lastName());
        author.setFirstName(faker.name().firstName());

        authors.add(author.getFirstName() + " " + author.getLastName());

        request.setIsbn("ISBN");
        request.setTitle(faker.book().title());
        request.setCatalog(catalog.getName());
        request.setAuthor(authors);
        request.setPublisher(faker.book().publisher());
        request.setGenre(faker.book().genre());
        request.setDescription("Another description");
        request.setLanguage(faker.nation().language());
        request.setPages(2000);
        request.setCopies(2);
        request.setPublishYear("1906");

        testBook.setIsbn(request.getIsbn());
        testBook.setTitle(request.getTitle());
        testBook.setCatalog(catalog);
        testBook.addAuthor(author);
        testBook.setPublisher(request.getPublisher());
        testBook.setGenre(request.getGenre());
        testBook.setDescription(request.getDescription());
        testBook.setLanguage(request.getLanguage());
        testBook.setPages(request.getPages());
        testBook.setCopies(request.getCopies());
        testBook.setPublishYear(request.getPublishYear());
        testBook.setCallNumber(testService.generateCallNumber(request.getTitle(), request.getCatalog(), request.getPublishYear()));
        testBook.setId(1L);


        expectedResponse.setId(testBook.getId());
        expectedResponse.setTitle(testBook.getTitle());
        expectedResponse.setCopies(testBook.getCopies());
        expectedResponse.setPages(testBook.getPages());
        expectedResponse.setDescription(testBook.getDescription());
        expectedResponse.setPublisher(testBook.getPublisher());
        expectedResponse.setCatalog(testBook.getCatalog().getName());
        expectedResponse.setPublishYear(testBook.getPublishYear());
        expectedResponse.setIsbn(testBook.getIsbn());
        expectedResponse.setGenre(testBook.getGenre());
        expectedResponse.setLanguage(testBook.getLanguage());
        expectedResponse.setCallNumber(testBook.getCallNumber());
        expectedResponse.setAuthorList(testBook.getBookAuthors());

        LocalDate dob = LocalDate.of(1997, 6, 16);

        testLibrarian.setAddress(faker.address().fullAddress());
        testLibrarian.setFirstName(faker.name().firstName());
        testLibrarian.setLastName(faker.name().lastName());
        testLibrarian.setPassword(faker.internet().password());
        testLibrarian.setEmail(faker.internet().emailAddress());
        testLibrarian.setPhoneNumber(faker.phoneNumber().phoneNumber());
        testLibrarian.setDateOfBirth(dob);
        testLibrarian.setId(UUID.randomUUID());

        testCard.setId(UUID.randomUUID());

        testMember.setAddress(faker.address().fullAddress());
        testMember.setFirstName(faker.name().firstName());
        testMember.setLastName(faker.name().lastName());
        testMember.setPassword(faker.internet().password());
        testMember.setEmail(faker.internet().emailAddress());
        testMember.setPhoneNumber(faker.phoneNumber().phoneNumber());
        testMember.setDateOfBirth(dob);
        testMember.setIsActive(true);
        testMember.setLibraryCard(testCard);
        testMember.setId(UUID.randomUUID());


    }

    @Test
    void canGenerateCallNumber() {
        //given
        String firstValue = testBook.getCatalog().getName().toUpperCase();
        String secondValue = testBook.getTitle().toUpperCase();
        String expected = firstValue + secondValue.substring(0, 3) + "1902";

        String response = testService.generateCallNumber(testBook.getTitle(), testBook.getCatalog().getName(), "1902");

        assertEquals(expected, response);
        assertEquals(expected.length(), response.length());
    }

    @Test
    void canGetAllBooks() {
        when(bookRepo.findAll()).thenReturn(Collections.singletonList(testBook));

        List<BookResponse> expected = new ArrayList<>();
        expected.add(expectedResponse);

        ResponseEntity<AppResponse<List<BookResponse>>> response = testService.getAllBooks();

        assertEquals(expected, Objects.requireNonNull(response.getBody()).getData());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().getStatus());
    }

    @Test
    void canGetEmptyBookList() {
        when(bookRepo.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<AppResponse<List<BookResponse>>> response = testService.getAllBooks();

        assertEquals("No books yet", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().getStatus());
    }

    @Test
    void canAddNewBook() {

        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.of(testLibrarian));
        when(catalogueRepo.findByName(request.getCatalog().toUpperCase())).thenReturn(Optional.of(catalog));

        ResponseEntity<AppResponse<BookResponse>> response = testService.newBook(testLibrarian.getId(), request);

        assertEquals(expectedResponse, Objects.requireNonNull(response.getBody()).getData());
        assertEquals(true, response.getBody().getStatus());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());


    }

    @Test
    void canNotAddNewBook() {

        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.empty());
        when(catalogueRepo.findByName(request.getCatalog().toUpperCase())).thenReturn(Optional.of(catalog));

        ResponseEntity<AppResponse<BookResponse>> response = testService.newBook(testLibrarian.getId(), request);

        assertEquals("Unauthorized", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(false, response.getBody().getStatus());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());


    }

    @Test
    void canNotFindBookCategory() {

        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.of(testLibrarian));
        when(catalogueRepo.findByName(request.getCatalog().toUpperCase())).thenReturn(Optional.empty());

        ResponseEntity<AppResponse<BookResponse>> response = testService.newBook(testLibrarian.getId(), request);

        assertEquals("Catalog does not exist!!", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(false, response.getBody().getStatus());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());


    }

    @Test
    void canEditBookDetails() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        List<String> addNewAuthors = new ArrayList<>();
        addNewAuthors.add(firstName + " " + lastName);

        Author newAuthor = new Author();
        newAuthor.setFirstName(firstName);
        newAuthor.setLastName(lastName);

        EditBookRequest editRequest = new EditBookRequest();
        editRequest.setCopies(1);
        editRequest.setTitle(faker.book().title());
        editRequest.setAddAuthor(addNewAuthors);
        editRequest.setDeleteAuthor(new ArrayList<>());
        editRequest.setDescription("Same description");

        testBook.setTitle(editRequest.getTitle());
        testBook.addAuthor(newAuthor);
        testBook.setDescription(editRequest.getDescription());

        expectedResponse.setCopies(testBook.getCopies() + editRequest.getCopies());
        expectedResponse.setTitle(testBook.getTitle());
        expectedResponse.setAuthorList(testBook.getAuthorList());
        expectedResponse.setDescription(testBook.getDescription());

        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.of(testLibrarian));
        when(bookRepo.findById(testBook.getId())).thenReturn(Optional.of(testBook));


        ResponseEntity<AppResponse<BookResponse>> response = testService.editBookDetails(testLibrarian.getId(), testBook.getId(), editRequest);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(expectedResponse, Objects.requireNonNull(response.getBody()).getData());


    }

    @Test
    void canNotEditBookDetails() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        List<String> addNewAuthors = new ArrayList<>();
        addNewAuthors.add(firstName + " " + lastName);

        Author newAuthor = new Author();
        newAuthor.setFirstName(firstName);
        newAuthor.setLastName(lastName);

        EditBookRequest editRequest = new EditBookRequest();
        editRequest.setCopies(1);
        editRequest.setTitle(faker.book().title());
        editRequest.setAddAuthor(addNewAuthors);
        editRequest.setDeleteAuthor(new ArrayList<>());
        editRequest.setDescription("Same description");

        testBook.setTitle(editRequest.getTitle());
        testBook.addAuthor(newAuthor);
        testBook.setDescription(editRequest.getDescription());


        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.empty());
        when(bookRepo.findById(testBook.getId())).thenReturn(Optional.of(testBook));


        ResponseEntity<AppResponse<BookResponse>> response = testService.editBookDetails(testLibrarian.getId(), testBook.getId(), editRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Unauthorized", Objects.requireNonNull(response.getBody()).getMessage());


    }

    @Test
    void canDeleteBook() {
        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.of(testLibrarian));
        when(bookRepo.findById(testBook.getId())).thenReturn(Optional.of(testBook));

        ResponseEntity<AppResponse<String>> response = testService.deleteBook(testLibrarian.getId(), testBook.getId());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("", Objects.requireNonNull(response.getBody()).getMessage());


    }

    @Test
    void canNotDeleteBook() {
        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.empty());
        when(bookRepo.findById(testBook.getId())).thenReturn(Optional.of(testBook));

        ResponseEntity<AppResponse<String>> response = testService.deleteBook(testLibrarian.getId(), testBook.getId());

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Unauthorized", Objects.requireNonNull(response.getBody()).getMessage());


    }

    @Test
    void canBorrowBookIfCopiesAvailable() {
        when(memberRepo.findById(testMember.getId())).thenReturn(Optional.of(testMember));
        when(bookRepo.findById(testBook.getId())).thenReturn(Optional.of(testBook));
        when(libraryCardRepo.findByMemberId(testMember.getId())).thenReturn(Optional.of(testCard));

        ResponseEntity<AppResponse<String>> response = testService.borrowBook(testBook.getId(), testMember.getId());

        assertEquals("Request successful", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void canNotBorrowBookIfCopiesNotAvailable() {
        when(memberRepo.findById(testMember.getId())).thenReturn(Optional.of(testMember));
        testBook.setCopies(0);
        when(bookRepo.findById(testBook.getId())).thenReturn(Optional.of(testBook));
        when(libraryCardRepo.findByMemberId(testMember.getId())).thenReturn(Optional.of(testCard));

        ResponseEntity<AppResponse<String>> response = testService.borrowBook(testBook.getId(), testMember.getId());

        assertEquals("No copies available. Try again later", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void canNotBorrowBookIfFinePresent() {
        when(memberRepo.findById(testMember.getId())).thenReturn(Optional.of(testMember));
        testCard.setFine(2.00);
        when(bookRepo.findById(testBook.getId())).thenReturn(Optional.of(testBook));
        when(libraryCardRepo.findByMemberId(testMember.getId())).thenReturn(Optional.of(testCard));

        ResponseEntity<AppResponse<String>> response = testService.borrowBook(testBook.getId(), testMember.getId());

        assertEquals("Please clear your fine", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void canNotBorrowBookIfMaxNumberOfBorrowedBooks() {
        when(memberRepo.findById(testMember.getId())).thenReturn(Optional.of(testMember));
        testCard.setMaxNumberOfBooks(2);
        testCard.setNumberOfBooksCheckedOut(3);
        when(bookRepo.findById(testBook.getId())).thenReturn(Optional.of(testBook));
        when(libraryCardRepo.findByMemberId(testMember.getId())).thenReturn(Optional.of(testCard));

        ResponseEntity<AppResponse<String>> response = testService.borrowBook(testBook.getId(), testMember.getId());

        assertEquals("Max number of books allowed exceeded. Return some and try again.", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void canNotBorrowBookIfAccountInActive() {
        when(memberRepo.findById(testMember.getId())).thenReturn(Optional.of(testMember));
        testMember.setIsActive(false);

        when(bookRepo.findById(testBook.getId())).thenReturn(Optional.of(testBook));
        when(libraryCardRepo.findByMemberId(testMember.getId())).thenReturn(Optional.of(testCard));

        ResponseEntity<AppResponse<String>> response = testService.borrowBook(testBook.getId(), testMember.getId());

        assertEquals("Activate your account", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

}
