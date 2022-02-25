package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Long> {
}
