package com.etz.libraryapi.domains.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;

@Data
@NoArgsConstructor
public class CreateNewCatalogRequest {
    @NotEmpty(message = "Required field")
    private ArrayList<String> catalogNames;

}
