package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.Catalog;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Slf4j
class CatalogueRepoTest {
    @Autowired
    private CatalogueRepo repo;

    @Test
    void testFindByName() {
        //given
        String name = "test";

        // when
        Optional<Catalog> catalog = repo.findByName(name);

        //then
        assertThat(catalog).isEmpty();
    }
}