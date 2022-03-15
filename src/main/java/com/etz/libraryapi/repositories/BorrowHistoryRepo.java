package com.etz.libraryapi.repositories;

import com.etz.libraryapi.models.BorrowHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BorrowHistoryRepo extends JpaRepository<BorrowHistory, UUID> {
}
