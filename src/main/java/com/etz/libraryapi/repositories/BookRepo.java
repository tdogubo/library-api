package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookRepo extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b JOIN FETCH b.authors a where b.title =?1 and a.firstName Like %?2% and a.lastName Like %?3%")
    Optional<Book> findByTitleAndAuthorFirstNameAndLastName(String title, String firstName, String lastName);
}
