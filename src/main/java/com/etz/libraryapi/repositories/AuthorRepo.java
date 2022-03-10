package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface AuthorRepo extends JpaRepository<Author, UUID> {
    @Query("SELECT a FROM Author a WHERE a.firstName = ?1 AND a.lastName =?2")
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
