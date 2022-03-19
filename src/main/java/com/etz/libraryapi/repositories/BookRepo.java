package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.Author;
import com.etz.libraryapi.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepo extends JpaRepository<Book, Long> {
//    @Query("select t from books t join t.authors u where u.username = :username")
//    Optional<Book> findAllByUsername(@Param("username") String username);

    Optional<Book> findByAuthors(Author authors);
}
