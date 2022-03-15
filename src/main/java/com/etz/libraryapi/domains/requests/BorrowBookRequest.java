package com.etz.libraryapi.domains.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
public class BorrowBookRequest {
    @NotNull(message = "Required field")
    private UUID memberId;
    @NotNull(message = "Required field")
    private String title;
    @NotNull(message = "Required field")
    private ArrayList<String> authors;
    private int tier;
}
