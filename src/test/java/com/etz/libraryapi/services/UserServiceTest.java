package com.etz.libraryapi.services;

import com.etz.libraryapi.domains.requests.CreateUserRequest;
import com.etz.libraryapi.domains.requests.LoginUserRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.UserResponse;
import com.etz.libraryapi.models.Librarian;
import com.etz.libraryapi.repositories.LibrarianRepo;
import com.etz.libraryapi.repositories.MemberRepo;
import com.etz.libraryapi.repositories.UserRepo;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
public class UserServiceTest {
    CreateUserRequest request = new CreateUserRequest();
    Librarian testUser = new Librarian();
    Faker faker = new Faker();
    @Autowired
    private UserService userServiceTest;
    @MockBean
    private UserRepo userRepo;
    @MockBean
    private LibrarianRepo librarianRepo;
    @MockBean
    private MemberRepo memberRepo;

    @BeforeEach
    void setUp() {
        Date dateTime = faker.date().birthday();
        LocalDate dob = dateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        request.setUserType("librarian");
        request.setFirstName(faker.name().firstName());
        request.setLastName(faker.name().lastName());
        request.setEmail(faker.internet().emailAddress());
        request.setPassword(faker.internet().password());
        request.setAddress(faker.address().fullAddress());
        request.setPhoneNumber(faker.phoneNumber().phoneNumber());
        request.setDateOfBirth(dob);

        testUser.setAddress(request.getAddress());
        testUser.setFirstName(request.getFirstName());
        testUser.setLastName(request.getLastName());
        testUser.setPassword(request.getPassword());
        testUser.setEmail(request.getEmail());
        testUser.setPhoneNumber(request.getPhoneNumber());
        testUser.setDateOfBirth(request.getDateOfBirth());
        testUser.setId(UUID.randomUUID());

    }

    @Test
    void canCreateUser() {
        when(userRepo.save(testUser)).thenReturn(testUser);
        ResponseEntity<AppResponse<UserResponse>> response = userServiceTest.createUser(request);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getFirstName(), Objects.requireNonNull(response.getBody()).getData().getFirstName());
        assertEquals(true, response.getBody().getStatus());


    }

    @Test
    void canLoginUser() {
        // if this test fails, comment out the password encoder in the user service for it to work.
        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setEmail(request.getEmail());
        loginRequest.setPassword(request.getPassword());

        //when
        when(librarianRepo.findByEmail(request.getEmail())).thenReturn(Optional.of(testUser));

        //then
        ResponseEntity<AppResponse<UserResponse>> response = userServiceTest.loginUser(loginRequest);

        assertEquals(loginRequest.getEmail(), Objects.requireNonNull(response.getBody()).getData().getEmail());
        assertEquals(HttpStatus.FOUND, response.getStatusCode());


    }

    @Test
    void canEditUser() {

    }

}
