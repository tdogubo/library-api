package com.etz.libraryapi.services;

import com.etz.libraryapi.domains.requests.ChangeUserStatusRequest;
import com.etz.libraryapi.domains.requests.CreateUserRequest;
import com.etz.libraryapi.domains.requests.EditUserRequest;
import com.etz.libraryapi.domains.requests.LoginUserRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.UserResponse;
import com.etz.libraryapi.models.Librarian;
import com.etz.libraryapi.models.Member;
import com.etz.libraryapi.repositories.LibrarianRepo;
import com.etz.libraryapi.repositories.MemberRepo;
import com.etz.libraryapi.repositories.UserRepo;
import com.github.javafaker.App;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
public class UserServiceTest {
    CreateUserRequest request = new CreateUserRequest();
    Librarian testLibrarian = new Librarian();
    Faker faker = new Faker();
    @Autowired
    private UserService userServiceTest;
    @MockBean
    private UserRepo userRepo;
    @MockBean
    private LibrarianRepo librarianRepo;
    @MockBean
    private MemberRepo testMemberRepo;

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

        testLibrarian.setAddress(request.getAddress());
        testLibrarian.setFirstName(request.getFirstName());
        testLibrarian.setLastName(request.getLastName());
        testLibrarian.setPassword(request.getPassword());
        testLibrarian.setEmail(request.getEmail());
        testLibrarian.setPhoneNumber(request.getPhoneNumber());
        testLibrarian.setDateOfBirth(request.getDateOfBirth());
        testLibrarian.setId(UUID.randomUUID());

    }

    @Test
    void canCreateUser() {
        when(userRepo.save(testLibrarian)).thenReturn(testLibrarian);
        ResponseEntity<AppResponse<UserResponse>> response = userServiceTest.createUser(request);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getFirstName(), Objects.requireNonNull(response.getBody()).getData().getFirstName());
        assertEquals(true, response.getBody().getStatus());


    }

    @Test
    void canNotCreateUser() {
        when(librarianRepo.findByEmail(request.getEmail())).thenReturn(Optional.of(testLibrarian));
        ResponseEntity<AppResponse<UserResponse>> response = userServiceTest.createUser(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email already exist", Objects.requireNonNull(response.getBody()).getMessage());


    }

    @Test
    void canLoginUser() {
        // if this test fails, comment out the password encoder in the user service for it to work.
        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setEmail(request.getEmail());
        loginRequest.setPassword(request.getPassword());

        //when
        when(librarianRepo.findByEmail(request.getEmail())).thenReturn(Optional.of(testLibrarian));

        //then
        ResponseEntity<AppResponse<UserResponse>> response = userServiceTest.loginUser(loginRequest);

        assertEquals(loginRequest.getEmail(), Objects.requireNonNull(response.getBody()).getData().getEmail());
        assertEquals(HttpStatus.FOUND, response.getStatusCode());


    }

    @Test
    void canNotLoginUser() {
        LoginUserRequest loginRequest = new LoginUserRequest();
        loginRequest.setEmail(request.getEmail());
        loginRequest.setPassword(request.getPassword());

        //when
        when(librarianRepo.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        //then
        ResponseEntity<AppResponse<UserResponse>> response = userServiceTest.loginUser(loginRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(false, Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals("User not found", Objects.requireNonNull(response.getBody()).getMessage());


    }

    @Test
    void canEditUser() {
        EditUserRequest request= new EditUserRequest();
        request.setPassword(faker.internet().password());
        request.setAddress(faker.address().fullAddress());
        request.setPhoneNumber(faker.phoneNumber().phoneNumber());

        when(librarianRepo.findById(any(UUID.class))).thenReturn(Optional.of(testLibrarian));

        ResponseEntity<AppResponse<UserResponse>> response = userServiceTest.editUser(UUID.randomUUID(),request);

        assertEquals(true, Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals(request.getAddress(), response.getBody().getData().getAddress());
        assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getStatusCode()));

    }

    @Test
    void canNotEditUser() {
        EditUserRequest request= new EditUserRequest();
        request.setPassword(faker.internet().password());
        request.setAddress(faker.address().fullAddress());
        request.setPhoneNumber(faker.phoneNumber().phoneNumber());

        when(librarianRepo.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<AppResponse<UserResponse>> response = userServiceTest.editUser(UUID.randomUUID(),request);

        assertEquals(false, Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals("User not found", response.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND, Objects.requireNonNull(response.getStatusCode()));

    }

    @Test
    void canChangeUserStatus(){
        // check method in user service
        ChangeUserStatusRequest request = new ChangeUserStatusRequest();
        request.setActivate(true);
        request.setLibrarianId(testLibrarian.getId());


        Member testMember = new Member();
        testMember.setEmail(faker.internet().emailAddress());
        testMember.setId(UUID.randomUUID());

        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.of(testLibrarian));
        when(testMemberRepo.findById(testMember.getId())).thenReturn(Optional.of(testMember));

        ResponseEntity<AppResponse<UserResponse>> response = userServiceTest.changeUserStatus(testMember.getId(), request);

        assertEquals(true, Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals(request.getActivate(), Objects.requireNonNull(response.getBody()).getData().isActive());

    }

    @Test
    void changeUserStatusFailByMemberId(){
        // check method in user service
        ChangeUserStatusRequest request = new ChangeUserStatusRequest();
        request.setActivate(true);
        request.setLibrarianId(testLibrarian.getId());


        Member testMember = new Member();
        testMember.setEmail(faker.internet().emailAddress());
        testMember.setId(UUID.randomUUID());

        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.of(testLibrarian));
        when(testMemberRepo.findById(testMember.getId())).thenReturn(Optional.empty());

        ResponseEntity<AppResponse<UserResponse>> response = userServiceTest.changeUserStatus(testMember.getId(), request);

        assertEquals(false, Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals("User not found", Objects.requireNonNull(response.getBody().getMessage()));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());


    }

    @Test
    void changeUserStatusFailByLibrarianId(){
        // check method in user service
        ChangeUserStatusRequest request = new ChangeUserStatusRequest();
        request.setActivate(true);
        request.setLibrarianId(testLibrarian.getId());


        Member testMember = new Member();
        testMember.setEmail(faker.internet().emailAddress());
        testMember.setId(UUID.randomUUID());

        when(librarianRepo.findById(testLibrarian.getId())).thenReturn(Optional.empty());
        when(testMemberRepo.findById(testMember.getId())).thenReturn(Optional.of(testMember));

        ResponseEntity<AppResponse<UserResponse>> response = userServiceTest.changeUserStatus(testMember.getId(), request);

        assertEquals(false, Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals(HttpStatus.FORBIDDEN, Objects.requireNonNull(response.getStatusCode()));
        assertEquals("Unauthorized", Objects.requireNonNull(response.getBody().getMessage()));



    }

}
