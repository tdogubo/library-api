package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.Librarian;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor
class LibrarianRepoTest {
    @Autowired
    private final LibrarianRepo repo;

    @Test
    void testFindByEmail() {
        //given
        String email = "test@email.com";

        // when
        Optional<Librarian> librarian = repo.findByEmail(email);

        //then
        assertThat(librarian).isEmpty();
    }
}