package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LibrarianRepo extends JpaRepository<Librarian, UUID> {
    Optional<Librarian> findByEmail(String email);


}
