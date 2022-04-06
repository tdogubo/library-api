package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatalogueRepo extends JpaRepository<Catalog, Long> {
    Optional<Catalog> findByName(String name);
}
