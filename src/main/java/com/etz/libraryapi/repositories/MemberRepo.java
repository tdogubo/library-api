package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepo extends JpaRepository<Member, UUID> {
    Optional<Member> findByEmail(String email);
}
