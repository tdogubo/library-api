package com.etz.libraryapi.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member")
public class Member extends User {
    private String cardNumber;

    private Boolean isActive;

    private int numberOfBooksCheckedOut;

    private int tier;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "librarian_id", referencedColumnName = "id")
    private Librarian librarian;

    @OneToOne
    @PrimaryKeyJoinColumn
    @JoinColumn(name = "card_id")
    private LibraryCard libraryCard;

}
