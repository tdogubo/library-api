package com.etz.libraryapi.domains.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class CatalogResponse {
    private Long id;

    private String name;

    private ArrayList<String> books;
}
