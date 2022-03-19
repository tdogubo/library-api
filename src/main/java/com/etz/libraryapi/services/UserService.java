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
import com.etz.libraryapi.models.LibraryCard;
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
        Optional<Librarian> existsLibrarian = librarianRepo.findByEmail(request.getEmail());
        Optional<Member> existsMember = memberRepo.findByEmail(request.getEmail());
        if (existsLibrarian.isPresent() || existsMember.isPresent()) {
            return new ResponseEntity<>(new AppResponse<>(false, "Email already exist"), HttpStatus.CONFLICT);

        } else {
            User user = request.getUserType().equals("librarian") ? new Librarian() : new Member();
            request.setPassword(encoder.passwordEncoder().encode(request.getPassword()));
            mapper.modelMapper().map(request, user);
            userRepo.save(user);
            UserResponse response = mapper.modelMapper().map(user, UserResponse.class);

            return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.CREATED);
        }

    }

    public ResponseEntity<AppResponse<UserResponse>> loginUser(LoginUserRequest request) {
        Optional<Librarian> librarian = librarianRepo.findByEmail(request.getEmail());
        Optional<Member> member = memberRepo.findByEmail(request.getEmail());
        if (librarian.isPresent()) {//&& encoder.passwordEncoder().matches(request.getPassword(), librarian.get().getPassword())) {
            Librarian foundUser = librarian.get();

            UserResponse response = mapper.modelMapper().map(foundUser, UserResponse.class);
            return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.FOUND);

        } else if (member.isPresent() && encoder.passwordEncoder().matches(request.getPassword(), member.get().getPassword())) {
            Member foundUser = member.get();
            UserResponse response = mapper.modelMapper().map(foundUser, UserResponse.class);
            return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.FOUND);

        }
        return new ResponseEntity<>(new AppResponse<>(false, "User not found"), HttpStatus.NOT_FOUND);


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
        return new ResponseEntity<>(new AppResponse<>(false, "User not found"), HttpStatus.NOT_FOUND);


    }

    public ResponseEntity<AppResponse<UserResponse>> changeUserStatus(UUID id, ChangeUserStatusRequest request) {
        Optional<Librarian> librarian = librarianRepo.findById(request.getLibrarianId());
        if (librarian.isPresent()) {
            Optional<Member> member = memberRepo.findById(id);
            if (member.isPresent()) {
                Member foundUser = member.get();
                foundUser.setIsActive(request.getActivate());
                LibraryCard libraryCard = new LibraryCard();
                libraryCard.setMember(foundUser);
                libraryCard.setTier(request.getTier() | libraryCard.getTier());
                memberRepo.save(foundUser);
                UserResponse response = mapper.modelMapper().map(foundUser, UserResponse.class);
                return new ResponseEntity<>(new AppResponse<>(true, response), HttpStatus.OK);
            }
            return new ResponseEntity<>(new AppResponse<>(false, "User not found"), HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(new AppResponse<>(false, "Unauthorized"), HttpStatus.FORBIDDEN);

    }

}
