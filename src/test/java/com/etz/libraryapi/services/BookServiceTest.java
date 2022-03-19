package com.etz.libraryapi.services;

import com.etz.libraryapi.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class BookServiceTest {
    @Autowired
    private BookService testService;
    @MockBean
    private BookRepo bookRepo;
    @MockBean
    private  AuthorRepo authorRepo;
    @MockBean
    private  MemberRepo memberRepo;
    @MockBean
    private  CatalogueRepo catalogueRepo;
    @MockBean
    private  LibrarianRepo librarianRepo;
}
