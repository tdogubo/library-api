package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.LibraryCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LibraryCardRepo extends JpaRepository<LibraryCard, UUID> {
}
