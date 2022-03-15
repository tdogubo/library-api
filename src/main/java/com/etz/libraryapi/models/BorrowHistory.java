package com.etz.libraryapi.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "borrow-history")
@Getter
@Setter
@NoArgsConstructor
public class BorrowHistory {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    private LocalDate creationDate = LocalDate.now();

    private LocalDate dueDate;

    private LocalDate returnDate;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
}
