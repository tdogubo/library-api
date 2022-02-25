package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
}
