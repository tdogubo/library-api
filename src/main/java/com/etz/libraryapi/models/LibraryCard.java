package com.etz.libraryapi.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
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

    @OneToOne(mappedBy = "libraryCard", cascade = CascadeType.ALL)
    @JoinColumn
    private Member member;
}
