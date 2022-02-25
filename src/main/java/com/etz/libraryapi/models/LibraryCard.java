package com.etz.libraryapi.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
public class LibraryCard {

    @Id
    @Column(name = "id")
    private UUID id;

    private int tier;

    @OneToOne(mappedBy = "libraryCard", cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn
    private Member member;
}
