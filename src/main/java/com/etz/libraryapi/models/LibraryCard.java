package com.etz.libraryapi.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class LibraryCard {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType") //without this, searching by UUID does not work
    private UUID id;

    private int tier = 1;

    private int maxNumberOfBooks = 3;

    private int numberOfBooksCheckedOut = 0;

    private LocalDate issuedAt = LocalDate.now();

    private double fine = 0.00;

    @OneToOne(mappedBy = "libraryCard", cascade = CascadeType.ALL)
    private Member member;

    @OneToOne
    @JoinColumn(name = "history_id", referencedColumnName = "id")
    private BorrowHistory history;
}
