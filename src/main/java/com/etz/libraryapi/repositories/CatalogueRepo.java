package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.Catalogue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogueRepo extends JpaRepository<Catalogue, Long> {
}
