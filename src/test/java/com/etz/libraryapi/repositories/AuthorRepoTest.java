package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.Author;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Slf4j

public class AuthorRepoTest {
    @Autowired
    private AuthorRepo authorTestRepo;

    @Test
    void testFindByFirstNameAndLastName() {
        //given
        Author saveAuthor = new Author();
        saveAuthor.setFirstName("John");
        saveAuthor.setLastName("Doe");
        authorTestRepo.save(saveAuthor);

        //when
        Optional<Author> author = authorTestRepo.findByFirstNameAndLastName("John", "Doe");

        //then
        assertThat(author.isPresent()).isTrue();
        assertThat(saveAuthor).isEqualTo(author.get());

    }

}
