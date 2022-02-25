package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepo extends JpaRepository<Author, Long> {
}
