package com.etz.libraryapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "librarians")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Librarian extends User {
    @OneToMany(mappedBy = "librarian", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Member> members;


}
