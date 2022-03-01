package com.etz.libraryapi.services;

import com.etz.libraryapi.config.Encoder;
import com.etz.libraryapi.config.Mapper;
import com.etz.libraryapi.domains.requests.ChangeUserStatusRequest;
import com.etz.libraryapi.domains.requests.CreateUserRequest;
import com.etz.libraryapi.domains.requests.EditUserRequest;
import com.etz.libraryapi.domains.requests.LoginUserRequest;
import com.etz.libraryapi.domains.responses.AppResponse;
import com.etz.libraryapi.domains.responses.UserResponse;
import com.etz.libraryapi.models.Librarian;
import com.etz.libraryapi.models.Member;
import com.etz.libraryapi.models.User;
import com.etz.libraryapi.repositories.LibrarianRepo;
import com.etz.libraryapi.repositories.MemberRepo;
import com.etz.libraryapi.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final LibrarianRepo librarianRepo;
    private final MemberRepo memberRepo;
    private final Mapper mapper;
    private final Encoder encoder;

    public ResponseEntity<AppResponse<UserResponse>> createUser(CreateUserRequest request) {
        User user = request.getUserType().equals("librarian") ? new Librarian() : new Member();
        request.setPassword(encoder.passwordEncoder().encode(request.getPassword()));
        mapper.modelMapper().map(request, user);
        userRepo.save(user);
        log.info("User created successfully" + user.getId());

        UserResponse response = mapper.modelMapper().map(user, UserResponse.class);

        return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.CREATED);
    }

    public ResponseEntity<AppResponse<UserResponse>> loginUser(LoginUserRequest request, HttpServletResponse cookieResponse) {
        Optional<Librarian> librarian = librarianRepo.findByEmail(request.getEmail());
        Optional<Member> member = memberRepo.findByEmail(request.getEmail());
        if (librarian.isPresent() && encoder.passwordEncoder().matches(request.getPassword(), librarian.get().getPassword())) {
            Librarian foundUser = librarian.get();

            UserResponse response = mapper.modelMapper().map(foundUser, UserResponse.class);
            Cookie cookie = new Cookie("id", foundUser.getId().toString()); // send the user id as cookie
            cookie.setHttpOnly(true);
            cookieResponse.addCookie(cookie);

            return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.FOUND);

        } else if (member.isPresent() && encoder.passwordEncoder().matches(request.getPassword(), member.get().getPassword())) {
            Member foundUser = member.get();
            UserResponse response = mapper.modelMapper().map(foundUser, UserResponse.class);
            return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.FOUND);

        }
        throw new IllegalStateException("User not found");

    }

    public ResponseEntity<AppResponse<UserResponse>> editUser(UUID id, EditUserRequest request) {
        request.setPassword(encoder.passwordEncoder().encode(request.getPassword()));
        Optional<Librarian> librarian = librarianRepo.findById(id);
        Optional<Member> member = memberRepo.findById(id);
        if (librarian.isPresent()) {
            Librarian foundUser = librarian.get();
            mapper.modelMapper().map(request, foundUser);
            librarianRepo.save(foundUser);
            UserResponse response = mapper.modelMapper().map(foundUser, UserResponse.class);
            return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.OK);
        } else if (member.isPresent()) {
            Member foundUser = member.get();
            mapper.modelMapper().map(request, foundUser);
            memberRepo.save(foundUser);
            UserResponse response = mapper.modelMapper().map(foundUser, UserResponse.class);
            return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.OK);
        }
        throw new IllegalStateException("User not found");

    }

    public ResponseEntity<AppResponse<UserResponse>> changeUserStatus(UUID id, ChangeUserStatusRequest request) {
        Optional<Member> member = memberRepo.findById(id);
        if (member.isPresent()) {
            Member foundUser = member.get();
            foundUser.setIsActive(request.getActivate());
            memberRepo.save(foundUser);
            UserResponse response = mapper.modelMapper().map(foundUser, UserResponse.class);
            return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.OK);
        }
        throw new IllegalStateException("User not found");
    }

}



