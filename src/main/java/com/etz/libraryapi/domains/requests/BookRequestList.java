package com.etz.libraryapi.domains.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
@NoArgsConstructor
public class BookRequestList {
    @NotNull(message = "Required field")
    private int tier;
    private ArrayList<BookRequest> books;
}
