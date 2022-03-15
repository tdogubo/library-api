package com.etz.libraryapi.domains.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CreateNewCatalogRequest {
    @NotNull(message = "Required field")
    private UUID librarianId;
    @NotEmpty(message = "Required field")
    private ArrayList<String> catalogNames;

}
