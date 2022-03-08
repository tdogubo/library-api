package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.Catalog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CatalogueRepoTest {
    @Autowired
    private CatalogueRepo repo;

    @Test
    void findByName() {
        //given
        String name = "test";

        // when
        Optional<Catalog> catalog = repo.findByName(name);

        //then
        assertThat(catalog.isPresent()).isFalse();
    }
}