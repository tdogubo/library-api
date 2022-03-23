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

    private Boolean isActive = false;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "librarian_id", referencedColumnName = "id")
    private Librarian librarian;

    @OneToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private LibraryCard libraryCard;



}
